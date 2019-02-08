package ir.acharkit.android.downloader;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import ir.acharkit.android.connection.ConnectionRequest;
import ir.acharkit.android.connection.ConnectionUtil;
import ir.acharkit.android.downloader.cacheDatabase.DownloaderDao;
import ir.acharkit.android.downloader.cacheDatabase.DownloaderModel;
import ir.acharkit.android.util.Logger;
import ir.acharkit.android.util.helper.MimeHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/6/2017
 * Email:   alirezat775@gmail.com
 */

public class Downloader {

    private static final String TAG = Downloader.class.getSimpleName();
    private DownloadTask downloadTask;

    private Downloader(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    /**
     * start download
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void download() {
        if (downloadTask == null)
            downloadTask = new DownloadTask();
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * cancel download
     */
    public void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancel();
            downloadTask = null;
        }
    }

    /**
     * resume download
     */
    public void resumeDownload() {
        if (downloadTask != null) {
            downloadTask.resume = true;
            download();
        }
    }

    /**
     * pause download
     */
    public void pauseDownload() {
        if (downloadTask != null) {
            downloadTask.resume = false;
            downloadTask.pause();
        }
    }

    private static class DownloadTask extends AsyncTask<Void, Void, Pair<Boolean, Exception>> {
        public SoftReference<Context> context;
        private DownloaderDao dao;
        private String downloadDir;
        private String fileName;
        private String extension;
        private String stringURL;
        private int timeOut;
        private OnDownloadListener downloadListener;
        private Map<String, String> header;
        private HttpURLConnection connection;
        private File downloadedFile;
        private int downloadedSize;
        private int finalPercent;
        private boolean resume = false;
        private boolean cancel = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!resume)
                dao.insertNewDownload(new DownloaderModel(0, stringURL, fileName, DownloaderModel.Status.NEW, 0));
        }

        @Override
        protected Pair<Boolean, Exception> doInBackground(Void... voids) {
            try {
                if (stringURL.trim().isEmpty()) {
                    throw new MalformedURLException("The entered URL is not valid");
                }
                URL url = new URL(stringURL);
                connection = (HttpURLConnection) url.openConnection();

                connection.setReadTimeout(timeOut == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : timeOut);
                connection.setConnectTimeout(timeOut == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : timeOut);
                connection.setRequestMethod(ConnectionRequest.Method.GET);

                ConnectionUtil.setHeaderParams(connection, header);

                connection.connect();
                connection.setInstanceFollowRedirects(false);
                int totalSize = connection.getContentLength();

                File fileDownloadDir = new File(downloadDir);
                if (!fileDownloadDir.exists()) {
                    boolean createdDir = fileDownloadDir.mkdirs();
                    Logger.d(TAG, "download directory is " + (createdDir ? "created" : "not created"));
                }

                downloadedFile = new File(downloadDir +
                        File.separator +
                        fileName +
                        "." +
                        extension);
                Logger.d(TAG, "downloading file path: " + downloadedFile.getPath());
                /* checking existing downloaded file previously
                   and checking length downloaded file vs new file from webservice
                 */
                if (downloadedFile.exists() && downloadedFile.length() == totalSize) {
                    return new Pair<>(true, null);
                }

                String contentType = String.valueOf(connection.getHeaderField("Content-Type"));
                if (contentType != null) {
                    if (fileName == null || fileName.trim().isEmpty()) {
                        fileName = (String.valueOf(System.currentTimeMillis()));
                        extension = MimeHelper.guessExtensionFromMimeType(contentType);
                    }
                } else {
                    if (fileName == null || fileName.trim().isEmpty()) {
                        fileName = (String.valueOf(System.currentTimeMillis()));
                        extension = "unknown";
                    }
                }

                /*
                  using openFileOutput for internal storage (because it needs to readable by every body)
                 */
                boolean onInternalStorage = downloadDir.startsWith("/data/data");
                FileOutputStream outputStream = onInternalStorage ?
                        context.get().openFileOutput(downloadedFile.getName(), Context.MODE_WORLD_READABLE) : new FileOutputStream(downloadedFile);

                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[32 * 1024];
                int len = 0;
                int previousPercent = -1;

                while ((len = inputStream.read(buffer)) > 0) {
                    if (isCancelled() || cancel)
                        return null;

                    outputStream.write(buffer, 0, len);
                    downloadedSize += len;

                    int percent = (int) (100.0f * (float) downloadedSize / totalSize);
                    Logger.d(TAG, "Downloading " + percent + "%");

                    if (downloadListener != null && previousPercent != percent) {
                        downloadListener.progressUpdate(percent, downloadedSize, totalSize);
                        previousPercent = percent;
                        finalPercent = percent;

                        dao.updateDownload(stringURL, DownloaderModel.Status.DOWNLOADING, finalPercent);

                        DownloaderModel model = dao.getDownload(stringURL);
                        Logger.d(TAG, "dao: url --> "
                                + model.getUrl()
                                + " id --> " + model.getId()
                                + " name --> " + model.getFileName()
                                + " status --> " + model.getStatus()
                                + " percent --> " + model.getPercent());
                    }
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                if (connection != null)
                    connection.disconnect();
                return new Pair<>(true, null);
            } catch (Exception e) {
                Logger.w(TAG, e);
                if (connection != null)
                    connection.disconnect();
                if (downloadedFile != null)
                    downloadedFile.delete();

                return new Pair<>(false, e);
            }
        }

        @Override
        protected void onPostExecute(Pair<Boolean, Exception> result) {
            super.onPostExecute(result);
            Logger.d(TAG, "download result is " + (result.first ? "success" : "failed"));

            if (downloadListener != null) {
                if (result.first) {
                    downloadListener.onCompleted(downloadedFile);
                    dao.updateDownload(stringURL, DownloaderModel.Status.SUCCESS, finalPercent);
                } else {
                    if (result.second instanceof ProtocolException || result.second instanceof MalformedURLException) {
                        Logger.w(TAG, result.second);
                    }
                    dao.updateDownload(stringURL, DownloaderModel.Status.FAIL, finalPercent);
                    downloadListener.onFailure(String.valueOf(result.second));
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        void cancel() {
            Logger.d(TAG, "download is cancelled");
            dao.updateDownload(stringURL, DownloaderModel.Status.FAIL, finalPercent);
            cancel = true;
            if (downloadedFile != null)
                downloadedFile.delete();
            if (downloadListener != null) {
                downloadListener.onCancel();
            }
            this.cancel(true);
        }

        void pause() {
            Logger.d(TAG, "download pause");
            dao.updateDownload(stringURL, DownloaderModel.Status.PAUSE, finalPercent);
            this.cancel(true);
            cancel = true;
        }
    }

    public static class Builder {
        private final Context context;
        private final String url;
        private int timeOut;
        private String downloadDir;
        private String fileName;
        private String extension;
        private OnDownloadListener downloadListener;
        private Map<String, String> header;
        private boolean resume = false;
        private boolean pause = false;

        /**
         * @param context the best practice is passing application context
         * @param url     passing url of the download file
         */
        public Builder(@NonNull Context context, @NonNull String url) {
            this.context = context;
            this.url = url;
        }

        /**
         * @param downloadDir for setting custom download directory (default value is sandbox/download/ directory)
         * @return builder
         */
        @CheckResult
        public Builder setDownloadDir(@NonNull String downloadDir) {
            this.downloadDir = downloadDir;
            return this;
        }

        /**
         * @param downloadListener an event listener for tracking download events
         * @return builder
         */
        @CheckResult
        public Builder setDownloadListener(@NonNull OnDownloadListener downloadListener) {
            this.downloadListener = downloadListener;
            return this;
        }

        /**
         * @param fileName  for saving with this name
         * @param extension extension of the file
         * @return builder
         */
        @CheckResult
        public Builder setFileName(@NonNull String fileName, @NonNull String extension) {
            this.fileName = fileName;
            this.extension = extension;
            return this;
        }

        /**
         * @param header for adding headers in http request
         * @return builder
         */
        @CheckResult
        public Builder setHeader(@NonNull Map<String, String> header) {
            this.header = header;
            return this;
        }

        /**
         * @param timeOut is a parameter for setting connection time out.
         * @return Builder
         */
        @CheckResult
        public Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * build downloader instance
         *
         * @return downloader instance
         */
        public Downloader build() {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.dao = new DownloaderDao(context);
            downloadTask.timeOut = timeOut;
            downloadTask.downloadListener = downloadListener;
            downloadTask.fileName = fileName;
            downloadTask.header = header;
            downloadTask.extension = extension;
            downloadTask.resume = resume;
            downloadTask.context = new SoftReference<>(context);
            if (downloadDir == null || downloadDir.trim().isEmpty())
                downloadTask.downloadDir = String.valueOf(context.getExternalFilesDir(null));
            else downloadTask.downloadDir = downloadDir;
            downloadTask.stringURL = url;

            return new Downloader(downloadTask);
        }
    }
//
//    public static class Builder {
//        private DownloaderDao dao;
//        private DownloadRequest downloadRequest;
//        private Context context;
//        private String downloadDir;
//        private String fileName;
//        private String extension;
//        private String url;
//        private int timeOut;
//        private int percent;
//        private boolean resume = false;
//        private OnDownloadListener downloadListener;
//        private Map<String, String> header;
//
//        /**
//         * @param context
//         * @param url
//         */
//        public Builder(@NonNull Context context, @NonNull String url) {
//            this.context = context;
//            this.url = url;
//            downloadRequest = new DownloadRequest();
//            dao = new DownloaderDao(context);
//        }
//
//        /**
//         * @return
//         */
//        public String getDownloadDir() {
//            return downloadDir;
//        }
//
//        /**
//         * @param downloadDir
//         * @return
//         */
//        @CheckResult
//        public Builder setDownloadDir(@NonNull String downloadDir) {
//            this.downloadDir = downloadDir;
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        private String getFileName() {
//            return fileName;
//        }
//
//        /**
//         * @return
//         */
//        private String getExtension() {
//            return extension;
//        }
//
//        /**
//         * @return
//         */
//        private String getUrl() {
//            return url;
//        }
//
//        /**
//         * @return
//         */
//        private OnDownloadListener getDownloadListener() {
//            return downloadListener;
//        }
//
//        /**
//         * @param downloadListener
//         * @return
//         */
//        @CheckResult
//        public Builder setDownloadListener(@NonNull OnDownloadListener downloadListener) {
//            this.downloadListener = downloadListener;
//            return this;
//        }
//
//        /**
//         * @param fileName
//         * @param extension
//         * @return
//         */
//        @CheckResult
//        public Builder setFileName(@NonNull String fileName, @NonNull String extension) {
//            this.fileName = fileName;
//            this.extension = extension;
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        private Context getContext() {
//            return context;
//        }
//
//        /**
//         * @return
//         */
//        private Map<String, String> getHeader() {
//            return header;
//        }
//
//        /**
//         * @param header
//         * @return
//         */
//        @CheckResult
//        public Builder setHeader(@NonNull Map<String, String> header) {
//            this.header = header;
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        private int getTimeOut() {
//            return timeOut;
//        }
//
//        /**
//         * @param timeOut
//         * @return
//         */
//        @CheckResult
//        public Builder setTimeOut(int timeOut) {
//            this.timeOut = timeOut;
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        private int getPercent() {
//            return percent;
//        }
//
//        /**
//         * @return
//         */
//        private void setPercent(int percent) {
//            this.percent = percent;
//        }
//
//        private void setResume(boolean resume) {
//            this.resume = resume;
//        }
//
//        private boolean isResume() {
//            return resume;
//        }
//
//        /**
//         * @param trust
//         * @return
//         */
//        @Deprecated
//        @CheckResult
//        public Builder trustSSL(boolean trust) {
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        @Deprecated
//        private boolean isTrust() {
//            return false;
//        }
//
//        /**
//         * @return
//         */
//        public Builder cancelDownload() {
//            downloadRequest.cancel(true);
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        public Builder pauseDownload() {
//            downloadRequest.cancel(true);
//            return this;
//        }
//
//        /**
//         * @return
//         */
//        public Builder resumeDownload() {
//            setResume(true);
//            download();
//            return this;
//        }
//
//        @RequiresPermission(Manifest.permission.INTERNET)
//        public Builder download() {
//            downloadRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            return this;
//        }
//
//        private class DownloadRequest extends AsyncTask<Void, Void, Void> {
//            private HttpURLConnection connection;
//            private int percent;
//            private int totalSize;
//            private File file;
//            private int downloadedSize;
//            private URL url;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                if (!isResume()) {
//                    dao.insertNewDownload(new DownloaderModel(0, getUrl(), getFileName(), DownloaderModel.Status.NEW, 0));
//                }
//            }
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    if (getUrl().trim().isEmpty()) {
//                        throw new MalformedURLException("The entered URL is not valid");
//                    }
//                    url = new URL(getUrl());
//                    String cookie = CookieManager.getInstance().getCookie(getUrl());
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestProperty("Cookie", cookie);
//                    connection.setReadTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
//                    connection.setConnectTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
//                    connection.setRequestMethod(ConnectionRequest.Method.GET);
//
//                    if (getDownloadDir() == null || getDownloadDir().trim().isEmpty()) {
//                        setDownloadDir(String.valueOf(getContext().getExternalFilesDir("download")));
//                    }
//
//                    connection.connect();
//                    connection.setInstanceFollowRedirects(false);
//                    totalSize = connection.getContentLength();
//
//                    String contentType = String.valueOf(connection.getHeaderField("Content-Type"));
//                    if (contentType != null) {
//                        if (getFileName() == null || getFileName().trim().isEmpty())
//                            setFileName(String.valueOf(System.currentTimeMillis()), MimeHelper.guessExtensionFromMimeType(contentType));
//                    } else {
//                        if (getFileName() == null || getFileName().trim().isEmpty())
//                            setFileName(String.valueOf(System.currentTimeMillis()), "unknown");
//                    }
//                    file = new File(getDownloadDir() + "/" + getFileName() + "." + getExtension());
//                    if (file.exists() && !isResume()) {
//                        file.delete();
//                    }
//                    FileOutputStream outputStream = new FileOutputStream(getDownloadDir() + "/" + getFileName() + "." + getExtension());
//                    InputStream inputStream = connection.getInputStream();
//                    byte[] buffer = new byte[32 * 1024];
//                    int len = 0;
//                    while ((len = inputStream.read(buffer)) > 0) {
//                        outputStream.write(buffer, 0, len);
//                        downloadedSize += len;
//
//                        percent = (int) (100.0f * (float) downloadedSize / totalSize);
//                        setPercent(percent);
//
//                        if (getDownloadListener() != null)
//                            getDownloadListener().progressUpdate(percent, downloadedSize, totalSize);
//
//                        dao.updateDownload(getUrl(), DownloaderModel.Status.DOWNLOADING, getPercent());
//
//                        Logger.d(TAG, "dao: url --> "
//                                + dao.getDownload(getUrl()).getUrl()
//                                + " id --> " + dao.getDownload(getUrl()).getId()
//                                + " name --> " + dao.getDownload(getUrl()).getFileName()
//                                + " status --> " + dao.getDownload(getUrl()).getStatus()
//                                + " percent --> " + dao.getDownload(getUrl()).getPercent());
//                    }
//                    outputStream.flush();
//                    outputStream.close();
//                    inputStream.close();
//                } catch (Exception e) {
//                    if (e instanceof ProtocolException || e instanceof MalformedURLException) {
//                        throw new RuntimeException("The entered protocol is not valid");
//                    }
//                    if (getDownloadListener() != null)
//                        getDownloadListener().onFailure(String.valueOf(e));
//
//                    connection.disconnect();
//                }
//                connection.disconnect();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//
//                if (getDownloadListener() != null)
//                    getDownloadListener().onCompleted(file);
//
//                dao.updateDownload(getUrl(), DownloaderModel.Status.SUCCESS, getPercent());
//
//                Logger.d(TAG, "dao: url --> "
//                        + dao.getDownload(getUrl()).getUrl()
//                        + " id --> " + dao.getDownload(getUrl()).getId()
//                        + " name --> " + dao.getDownload(getUrl()).getFileName()
//                        + " status --> " + dao.getDownload(getUrl()).getStatus()
//                        + " percent --> " + dao.getDownload(getUrl()).getPercent());
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//                if (connection != null)
//                    connection.disconnect();
//                if (getDownloadListener() != null) {
//                    getDownloadListener().onFailure("Request cancelled");
//                }
//                if (file != null && file.exists()) {
//                    file.delete();
//                }
//                dao.updateDownload(getUrl(), DownloaderModel.Status.FAIL, percent);
//            }
//        }
//    }
}
