package ir.acharkit.android.imageLoader;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.webkit.CookieManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedHashMap;

import javax.net.ssl.HttpsURLConnection;

import ir.acharkit.android.connection.ConnectionRequest;
import ir.acharkit.android.connection.ConnectionUtil;
import ir.acharkit.android.util.helper.MimeHelper;
import ir.acharkit.android.util.helper.StringHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    1/18/18
 * Email:   alirezat775@gmail.com
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getName();
    private static LinkedHashMap<String, Bitmap> hashBitmapList = new LinkedHashMap<>(0, 0.75f, true);

    public static class Builder {
        private Context context;
        private String url;
        private String downloadDir;
        private String fileName;
        private String extension;
        private ImageView imageView;
        private int placeHolder;
        private int timeOut;
        private boolean trust = false;
        private OnImageLoadListener onImageLoadListener;
        private ImageLoadRequest imageLoadRequest;
        private String hashName;

        /**
         * @param context
         * @param imageView
         */
        public Builder(Context context, ImageView imageView) {
            this.context = context;
            this.imageView = imageView;
        }

        /**
         * @return
         */
        public String getDownloadDir() {
            if (downloadDir == null) {
                downloadDir = (String.valueOf(getContext().getExternalFilesDir("imageLoader")));
            }
            return downloadDir;
        }

        /**
         * @param downloadDir
         * @return
         */
        public Builder setDownloadDir(String downloadDir) {
            this.downloadDir = downloadDir;
            return this;
        }

        /**
         * @return
         */
        public String getUrl() {
            return url;
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
        private String getFileName() {
            return fileName;
        }

        /**
         * @return
         */

        public String getExtension() {
            return extension;
        }

        /**
         * @return
         */
        public ImageView getImageView() {
            return imageView;
        }

        /**
         * @return
         */
        public int getPlaceHolder() {
            return placeHolder;
        }

        /**
         * @param placeHolder
         * @return
         */
        public Builder setPlaceHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        /**
         * @param fileName
         * @param extension
         * @return
         */
        private Builder setFileName(@NonNull String fileName, @NonNull String extension) {
            this.fileName = fileName;
            this.extension = extension;
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
        public Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * @return
         */
        private OnImageLoadListener getOnImageLoadListener() {
            return onImageLoadListener;
        }

        /**
         * @param onImageLoadListener
         * @return
         */
        public Builder setOnImageLoadListener(OnImageLoadListener onImageLoadListener) {
            this.onImageLoadListener = onImageLoadListener;
            return this;
        }

        /**
         * @return
         */
        private boolean isTrust() {
            return trust;
        }

        /**
         * @param trust
         */
        public Builder setTrust(boolean trust) {
            this.trust = trust;
            return this;
        }

        /**
         * @return
         */
        @RequiresPermission(Manifest.permission.INTERNET)
        public Builder load(String url) {
            this.url = url;
            hashName = StringHelper.SHA1(url);

            if (hashBitmapList.get(hashName) != null) {
                loadImageBitmap(hashBitmapList.get(hashName));
            } else {
                if (getPlaceHolder() != 0)
                    getImageView().setImageResource(getPlaceHolder());

                File file = new File(getDownloadDir() + "/" + hashName + "." + MimeHelper.getFileExtensionFromUrl(getUrl()));
                if (file.exists()) {
                    loadImageFile(hashName);
                } else {
                    setFileName(StringHelper.SHA1(getUrl()), MimeHelper.getFileExtensionFromUrl(getUrl()));
                    imageLoadRequest = new ImageLoadRequest();
                    imageLoadRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
            return this;
        }

        /**
         * @param hashName
         * @return
         */
        private Bitmap getBitmap(String hashName) {
            if (getExtension() == null || getExtension().isEmpty())
                return BitmapFactory.decodeFile(getDownloadDir() + "/" + hashName + "." + MimeHelper.getFileExtensionFromUrl(getUrl()));
            else
                return BitmapFactory.decodeFile(getDownloadDir() + "/" + hashName + "." + getExtension());
        }

        /**
         * @param hashName
         */
        private void loadImageFile(String hashName) {
            Bitmap bitmap = getBitmap(hashName);
            getImageView().setImageBitmap(bitmap);
            hashBitmapList.put(hashName, bitmap);
        }


        /**
         * @param bitmap
         */
        private void loadImageBitmap(Bitmap bitmap) {
            getImageView().setImageBitmap(bitmap);
        }

        /**
         * @return
         */
        public Builder cleanCache(OnCleanCacheListener cleanCacheListener) {
            hashBitmapList.clear();
            ImageLoaderUtil.cleanCache(getDownloadDir(), cleanCacheListener);
            return this;
        }

        private class ImageLoadRequest extends AsyncTask<Void, Void, Void> {
            private HttpURLConnection connection;
            private URL url;
            private String hashName = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (getUrl().trim().isEmpty()) {
                        throw new MalformedURLException("The entered URL is not valid");
                    }

                    hashName = StringHelper.SHA1(getUrl());
                    url = new URL(getUrl());
                    String cookie = CookieManager.getInstance().getCookie(getUrl());
                    if (isTrust()) {
                        if (url.getProtocol().toLowerCase().equals("https")) {
                            ConnectionUtil.trustHosts();
                            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                            https.setHostnameVerifier(ConnectionUtil.DO_NOT_VERIFY);
                            connection = https;
                        } else {
                            connection = (HttpURLConnection) url.openConnection();
                        }
                    } else {
                        connection = (HttpURLConnection) url.openConnection();
                    }

                    connection.setRequestProperty("Cookie", cookie);
                    connection.setReadTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
                    connection.setConnectTimeout(getTimeOut() == 0 ? ConnectionUtil.TIME_OUT_CONNECTION : getTimeOut());
                    connection.setRequestMethod(ConnectionRequest.Method.GET);

                    connection.connect();

                    connection.setInstanceFollowRedirects(false);

                    FileOutputStream outputStream = new FileOutputStream(getDownloadDir() + "/" + getFileName() + "." + getExtension());
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[32 * 1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    if (e instanceof ProtocolException || e instanceof MalformedURLException) {
                        throw new RuntimeException("The entered protocol is not valid");
                    }
                    if (getOnImageLoadListener() != null)
                        getOnImageLoadListener().onFailure(String.valueOf(e));

                    connection.disconnect();
                }
                connection.disconnect();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadImageFile(hashName);
                if (getOnImageLoadListener() != null)
                    getOnImageLoadListener().onCompleted(getImageView());
            }
        }
    }
}