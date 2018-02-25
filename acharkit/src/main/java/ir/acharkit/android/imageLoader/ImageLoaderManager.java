package ir.acharkit.android.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.webkit.URLUtil;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import ir.acharkit.android.ImageLoader.cache.DiskCache;
import ir.acharkit.android.ImageLoader.cache.MemoryCache;
import ir.acharkit.android.util.helper.MimeHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/24/18
 * Email:   alirezat775@gmail.com
 */
class ImageLoaderManager {

    private static final String TAG = ImageLoaderManager.class.getName();
    static Map<ImageView, String> imageViewList = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ImageView imageView;
    private Context context;
    private DiskCache diskCache;
    private MemoryCache memoryCache;
    private OnImageLoaderListener listener;

    ImageLoaderManager(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    private OnImageLoaderListener getListener() {
        return listener;
    }

    void setListener(OnImageLoaderListener listener) {
        this.listener = listener;
    }

    private MemoryCache getMemoryCache() {
        return memoryCache;
    }

    void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    private DiskCache getDiskCache() {
        return diskCache;
    }

    void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    void load(String url) {
        imageViewList.put(getImageView(), url);
        Loader loader = new Loader(getImageView(), url);
        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private ImageView getImageView() {
        return imageView;
    }

    private boolean hasImage(ImageView imageView, String url) {
        String tag = imageViewList.get(imageView);
        return tag == null || !tag.equals(url);
    }

    private void showImage(final String url, final ImageView imageView, final Bitmap bitmap) {
        if (hasImage(imageView, url)) {
            if (getListener() != null) getListener().onCompleted(imageView, url, bitmap);
            return;
        }
        if (bitmap != null) {
            getMemoryCache().put(url, bitmap);
            imageView.setImageBitmap(bitmap);
            if (getListener() != null) getListener().onCompleted(imageView, url, bitmap);
        }
    }

    public Context getContext() {
        return context;
    }

    private class Loader extends AsyncTask<Void, Void, Void> {
        Bitmap bmp;

        private ImageView imageView;
        private String url;

        Loader(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // load from memoryCache
            Bitmap bitmap = getMemoryCache().get(url);
            if (bitmap != null) {
                bmp = bitmap;
                return null;
            }

            // load from storage
            File diskCacheFile = getDiskCache().getFile(url, MimeHelper.getFileExtensionFromUrl(url));
            bitmap = ImageLoaderUtil.decodeBitmap(diskCacheFile);
            if (bitmap != null) {
                bmp = bitmap;
                return null;
            }

            // load from resources
            if (url.startsWith("@")) {
                int imageResource = getContext().getResources().getIdentifier(url, null, getContext().getPackageName());
                bmp = ImageLoaderUtil.getBitmapVectorDrawable(getContext(), imageResource);
                return null;
            }

            // load image from internet
            if (URLUtil.isValidUrl(url)) {
                try {
                    String extension = MimeHelper.getFileExtensionFromUrl(url);
                    File file = new File(getDiskCache().getCacheDir(), ImageLoaderUtil.getHashName(url) + "." + extension);
                    ImageLoaderUtil.copyInputStreamToFile(new URL(url).openConnection().getInputStream(), file);
                    bmp = ImageLoaderUtil.decodeBitmap(file);
                } catch (IOException e) {
                    getListener().onFailure(e.getMessage());
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showImage(url, imageView, bmp);
        }
    }
}
