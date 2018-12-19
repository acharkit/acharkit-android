package ir.acharkit.android.downloader;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.webkit.CookieManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import ir.acharkit.android.connection.ConnectionRequest;
import ir.acharkit.android.connection.ConnectionUtil;
import ir.acharkit.android.downloader.cacheDatabase.DownloaderDao;
import ir.acharkit.android.downloader.cacheDatabase.DownloaderModel;
import ir.acharkit.android.util.Log;
import ir.acharkit.android.util.helper.MimeHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/6/2017
 * Email:   alirezat775@gmail.com
 */

public abstract class Downloader {
    private static final String TAG = Downloader.class.getSimpleName();

    public static class Builder {
        private DownloaderDao dao;
        private DownloadRequest downloadRequest;
        private Context context;
        private String downloadDir;
        private String fileName;
        private String extension;
        private String url;
        private int timeOut;
        private int percent;
        private boolean resume = false;
        private OnDownloadListener downloadListener;
        private Map<String, String> header;

        /**
         * @param context
         * @param url
         */
        public Builder(@NonNull Context context, @NonNull String url) {
            this.context = context;
            this.url = url;
            downloadRequest = new DownloadRequest();
            dao = new DownloaderDao(context);
        }

        /**
         * @return
         */
        public String getDownloadDir() {
            return downloadDir;
        }

        /**
         * @param downloadDir
         * @return
         */
        @CheckResult
        public Builder setDownloadDir(@NonNull String downloadDir) {
            this.downloadDir = downloadDir;
            return this;
        }

        /**
         * @return
         */
        private String getFileName() {
            return fileName;
        }

        /**
         * @return
         */
        private String getExtension() {
            return extension;
        }

        /**
         * @return
         */
        private String getUrl() {
            return url;
        }

        /**
         * @return
         */
        private OnDownloadListener getDownloadListener() {
            return downloadListener;
        }

        /**
         * @param downloadListener
         * @return
         */
        @CheckResult
        public Builder setDownloadListener(@NonNull OnDownloadListener downloadListener) {
            this.downloadListener = downloadListener;
            return this;
        }

        /**
         * @param fileName
         * @param extension
         * @return
         */
        @CheckResult
        public Builder setFileName(@NonNull String fileName, @NonNull String extension) {
            this.fileName = fileName;
            this.extension = extension;
            return this;
        }

        /**
         * @return
         */
        private Context getContext() {
            return context;
        }

        /**
         * @return
         */
        private Map<String, String> getHeader() {
            return header;
        }

        /**
         * @param header
         * @return
         */
        @CheckResult
        public Builder setHeader(@NonNull Map<String, String> header) {
            this.header = header;
            return this;
        }

        /**
         * @return
         */
        private int getTimeOut() {
            return timeOut;
        }

        /**
         * @param timeOut
         * @return
         */
        @CheckResult
        public Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @return
         */
        private int getPercent() {
            return percent;
        }

        /**
         * @return
         */
        private void setPercent(int percent) {
            this.percent = percent;
        }

        private void setResume(boolean resume) {
            this.resume = resume;
        }

        private boolean isResume() {
            return resume;
        }

        /**
         * @param trust
         * @return
         */
        @Deprecated
        @CheckResult
        public Builder trustSSL(boolean trust) {
            return this;
        }

        /**
         * @return
         */
        @Deprecated
        private boolean isTrust() {
            return false;
        }

        /**
         * @return
         */
        public Builder cancelDownload() {
            downloadRequest.cancel(true);
            return this;
        }

        /**
         * @return
         */
        public Builder pauseDownload() {
            downloadRequest.cancel(true);
            return this;
        }

        /**
         * @return
         */
        public Builder resumeDownload() {
            setResume(true);
            download();
            return this;
        }

        @RequiresPermission(Manifest.permission.INTERNET)
        public Builder download() {
            downloadRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return this;
        }

        private class DownloadRequest extends AsyncTask<Void, Void, Void> {
            private HttpURLConnection connection;
            private int percent;
            private int totalSize;
            private File file;
            private int downloadedSize;
            private URL url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!isResume()) {
                    dao.insertNewDownload(new DownloaderModel(0, getUrl(), getFileName(), DownloaderModel.Status.NEW, 0));
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (getUrl().trim().isEmpty()) {
                        throw new MalformedURLException("The entered URL is not valid");
                    }
                    url = new URL(getUrl());
                    String cookie = CookieManager.getInstance().getCookie(getUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Cookie", cookie);
                    connection.setReadTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
                    connection.setConnectTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
                    connection.setRequestMethod(ConnectionRequest.Method.GET);

                    if (getDownloadDir() == null || getDownloadDir().trim().isEmpty()) {
                        setDownloadDir(String.valueOf(getContext().getExternalFilesDir("download")));
                    }

                    connection.connect();
                    connection.setInstanceFollowRedirects(false);
                    totalSize = connection.getContentLength();

                    String contentType = String.valueOf(connection.getHeaderField("Content-Type"));
                    if (contentType != null) {
                        if (getFileName() == null || getFileName().trim().isEmpty())
                            setFileName(String.valueOf(System.currentTimeMillis()), MimeHelper.guessExtensionFromMimeType(contentType));
                    } else {
                        if (getFileName() == null || getFileName().trim().isEmpty())
                            setFileName(String.valueOf(System.currentTimeMillis()), "unknown");
                    }
                    file = new File(getDownloadDir() + "/" + getFileName() + "." + getExtension());
                    if (file.exists() && !isResume()) {
                        file.delete();
                    }
                    FileOutputStream outputStream = new FileOutputStream(getDownloadDir() + "/" + getFileName() + "." + getExtension());
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[32 * 1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                        downloadedSize += len;

                        percent = (int) (100.0f * (float) downloadedSize / totalSize);
                        setPercent(percent);

                        if (getDownloadListener() != null)
                            getDownloadListener().progressUpdate(percent, downloadedSize, totalSize);

                        dao.updateDownload(getUrl(), DownloaderModel.Status.DOWNLOADING, getPercent());

                        Log.d(TAG, "dao: url --> "
                                + dao.getDownload(getUrl()).getUrl()
                                + " id --> " + dao.getDownload(getUrl()).getId()
                                + " name --> " + dao.getDownload(getUrl()).getFileName()
                                + " status --> " + dao.getDownload(getUrl()).getStatus()
                                + " percent --> " + dao.getDownload(getUrl()).getPercent());
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (Exception e) {
                    if (e instanceof ProtocolException || e instanceof MalformedURLException) {
                        throw new RuntimeException("The entered protocol is not valid");
                    }
                    if (getDownloadListener() != null)
                        getDownloadListener().onFailure(String.valueOf(e));

                    connection.disconnect();
                }
                connection.disconnect();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (getDownloadListener() != null)
                    getDownloadListener().onCompleted(file);

                dao.updateDownload(getUrl(), DownloaderModel.Status.SUCCESS, getPercent());

                Log.d(TAG, "dao: url --> "
                        + dao.getDownload(getUrl()).getUrl()
                        + " id --> " + dao.getDownload(getUrl()).getId()
                        + " name --> " + dao.getDownload(getUrl()).getFileName()
                        + " status --> " + dao.getDownload(getUrl()).getStatus()
                        + " percent --> " + dao.getDownload(getUrl()).getPercent());
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
                if (connection != null)
                    connection.disconnect();
                if (getDownloadListener() != null) {
                    getDownloadListener().onFailure("Request cancelled");
                }
                if (file != null && file.exists()) {
                    file.delete();
                }
                dao.updateDownload(getUrl(), DownloaderModel.Status.FAIL, percent);
            }
        }
    }
}
