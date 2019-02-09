package ir.acharkit.android.downloader;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
        if (this.downloadTask == null) {
            this.downloadTask = downloadTask;
        }
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public void download() {
        if (downloadTask == null) {
            throw new IllegalStateException("rebuild new instance after \"pause or cancel\" download");
        }
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancel();
            downloadTask = null;
        }
    }

    public void pauseDownload() {
        if (downloadTask != null) {
            downloadTask.pause();
            downloadTask = null;
        }
    }

    public void resumeDownload() {
        if (downloadTask != null) downloadTask.resume = true;
        download();
    }

    private static class DownloadTask extends AsyncTask<Void, Void, Pair<Boolean, Exception>> {
        public SoftReference<Context> context;
        boolean resume = false;
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
        private int percent;
        private int totalSize;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!resume)
                dao.insertNewDownload(new DownloaderModel(0, stringURL, fileName, DownloaderModel.Status.NEW, 0, 0, 0));
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

                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }

                if (resume) {
                    DownloaderModel model = dao.getDownload(stringURL);
                    percent = model.getPercent();
                    downloadedSize = model.getSize();
                    totalSize = model.getTotalSize();
                    connection.setAllowUserInteraction(true);
                    connection.setRequestProperty("Range", "bytes=" + model.getSize() + "-");
                }

                connection.connect();

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

                if (!resume) totalSize = connection.getContentLength();
                File fileDownloadDir = new File(downloadDir);
                if (!fileDownloadDir.exists()) {
                    boolean createdDir = fileDownloadDir.mkdirs();
                    Logger.d(TAG, "download directory is " + (createdDir ? "created" : "not created"));
                }

                downloadedFile = new File(downloadDir + File.separator + fileName + "." + extension);
                Logger.d(TAG, "downloading file path: " + downloadedFile.getPath());
                if (downloadedFile.exists() && downloadedFile.length() == totalSize) {
                    return new Pair<>(true, null);
                }

                BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream fileOutputStream = (downloadedSize == 0) ? new FileOutputStream(downloadedFile) : new FileOutputStream(downloadedFile, true);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);

                byte[] buffer = new byte[32 * 1024];
                int len = 0;
                int previousPercent = -1;
                while ((len = bufferedInputStream.read(buffer, 0, 1024)) >= 0 && !isCancelled()) {
                    bufferedOutputStream.write(buffer, 0, len);
                    downloadedSize += len;
                    percent = (int) (100.0f * (float) downloadedSize / totalSize);
                    if (downloadListener != null && previousPercent != percent) {
                        downloadListener.progressUpdate(percent, downloadedSize, totalSize);
                        previousPercent = percent;
                        Logger.d(TAG, "Downloading " + percent + "%");
                        DownloaderModel model = dao.getDownload(stringURL);
                        dao.updateDownload(stringURL, DownloaderModel.Status.DOWNLOADING, percent, downloadedSize, totalSize);
                        Logger.d(TAG, "dao:"
                                + " url     --> " + model.getUrl()
                                + " id      --> " + model.getId()
                                + " name    --> " + model.getFileName()
                                + " status  --> " + model.getStatus()
                                + " percent --> " + model.getPercent());

                    }
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bufferedInputStream.close();
                if (connection != null) connection.disconnect();
                return new Pair<>(true, null);
            } catch (Exception e) {
                Logger.w(TAG, e);
                if (connection != null) connection.disconnect();
                if (downloadedFile != null) downloadedFile.delete();
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
                    dao.updateDownload(stringURL, DownloaderModel.Status.SUCCESS, percent, downloadedSize, totalSize);
                    DownloaderModel model = dao.getDownload(stringURL);
                    Logger.d(TAG, "dao:"
                            + " url     --> " + model.getUrl()
                            + " id      --> " + model.getId()
                            + " name    --> " + model.getFileName()
                            + " status  --> " + model.getStatus()
                            + " percent --> " + model.getPercent());
                } else {
                    if (result.second instanceof ProtocolException || result.second instanceof MalformedURLException) {
                        Logger.w(TAG, result.second);
                    }
                    downloadListener.onFailure(String.valueOf(result.second));
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (connection != null) connection.disconnect();
        }

        @Override
        protected void onCancelled(Pair<Boolean, Exception> booleanExceptionPair) {
            super.onCancelled(booleanExceptionPair);
            if (connection != null) connection.disconnect();
        }

        void cancel() {
            Logger.d(TAG, "download is cancelled");
            if (downloadedFile != null) downloadedFile.delete();
            downloadListener.onCancel();
            cancel(true);
            dao.updateDownload(stringURL, DownloaderModel.Status.FAIL, percent, downloadedSize, totalSize);
        }

        void pause() {
            Logger.d(TAG, "download is pause");
            cancel(true);
            dao.updateDownload(stringURL, DownloaderModel.Status.PAUSE, percent, downloadedSize, totalSize);
        }
    }

    public static class Builder {

        private final Context mContext;
        private final String mUrl;
        private int mTimeOut;
        private String mDownloadDir;
        private String mFileName;
        private String mExtension;
        private OnDownloadListener mDownloadListener;
        private Map<String, String> mHeader;

        /**
         * @param context the best practice is passing application context
         * @param url     passing url of the download file
         */
        public Builder(@NonNull Context context, @NonNull String url) {
            this.mContext = context;
            this.mUrl = url;
        }

        /**
         * @param downloadDir for setting custom download directory (default value is sandbox/download/ directory)
         * @return builder
         */
        @CheckResult
        public Builder setDownloadDir(@NonNull String downloadDir) {
            this.mDownloadDir = downloadDir;
            return this;
        }


        /**
         * @param downloadListener an event listener for tracking download events
         * @return builder
         */
        @CheckResult
        public Builder setDownloadListener(@NonNull OnDownloadListener downloadListener) {
            this.mDownloadListener = downloadListener;
            return this;
        }

        /**
         * @param fileName  for saving with this name
         * @param extension extension of the file
         * @return builder
         */
        @CheckResult
        public Builder setFileName(@NonNull String fileName, @NonNull String extension) {
            this.mFileName = fileName;
            this.mExtension = extension;
            return this;
        }

        /**
         * @param header for adding headers in http request
         * @return builder
         */
        @CheckResult
        public Builder setHeader(@NonNull Map<String, String> header) {
            this.mHeader = header;
            return this;
        }

        /**
         * @param timeOut is a parameter for setting connection time out.
         * @return Builder
         */
        @CheckResult
        public Builder setTimeOut(int timeOut) {
            this.mTimeOut = timeOut;
            return this;
        }

        public Downloader build() {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.dao = new DownloaderDao(mContext);
            ;
            downloadTask.timeOut = mTimeOut;
            downloadTask.downloadListener = mDownloadListener;
            downloadTask.fileName = mFileName;
            downloadTask.header = mHeader;
            downloadTask.extension = mExtension;
            downloadTask.context = new SoftReference<>(mContext);
            if (mDownloadDir == null || mDownloadDir.trim().isEmpty()) {
                downloadTask.downloadDir = String.valueOf(mContext.getExternalFilesDir(null));
            } else
                downloadTask.downloadDir = mDownloadDir;
            downloadTask.stringURL = mUrl;

            return new Downloader(downloadTask);
        }
    }

}

