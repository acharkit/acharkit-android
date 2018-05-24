package ir.acharkit.android.imageLoader;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import ir.acharkit.android.imageLoader.cache.DiskCache;
import ir.acharkit.android.imageLoader.cache.MemoryCache;
import ir.acharkit.android.imageLoader.cache.OnCacheListener;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/2/18
 * Email:   alirezat775@gmail.com
 */
public abstract class ImageLoader {

    private static final String TAG = ImageLoader.class.getName();
    static MemoryCache memoryCache = new MemoryCache();

    public static class Builder {
        private OnImageLoaderListener imageLoaderListener;
        private DiskCache diskCache;
        private String downloadDir;
        private Context context;
        private int placeHolder;

        /**
         * @param context
         * @param downloadDir
         */
        public Builder(@NonNull Context context, String downloadDir) {
            this.context = context;
            this.downloadDir = downloadDir;
            diskCache = DiskCache.getInstance(context, getDownloadDir());
        }

        /**
         * @return
         */
        private String getDownloadDir() {
            return downloadDir;
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
        private int getPlaceHolder() {
            return placeHolder;
        }

        /**
         * @param placeHolder
         */
        @CheckResult
        public Builder setPlaceHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        /**
         * @param imageView
         * @param url
         */
        public void load(final ImageView imageView, final String url) {
            if (getImageLoaderListener() != null)
                getImageLoaderListener().onStart(imageView, url);

            ImageLoaderManager loaderManager = new ImageLoaderManager(context, imageView);
            loaderManager.setDiskCache(diskCache);
            loaderManager.setMemoryCache(memoryCache);
            loaderManager.setListener(getImageLoaderListener());
            loaderManager.load(url);

            if (getPlaceHolder() != 0)
                imageView.setImageResource(getPlaceHolder());
        }

        /**
         * @return
         */
        public void clearCache(final OnCacheListener cacheListener) {
            ImageLoaderManager.imageViewList.clear();
            memoryCache.clear();
            diskCache.clear(new DiskCache.CacheClear() {
                @Override
                public void clear() {
                    cacheListener.onCompleted();
                }
            });
        }

        /**
         * @return
         */
        public void clearCache() {
            ImageLoaderManager.imageViewList.clear();
            memoryCache.clear();
            diskCache.clear(null);
        }

        /**
         * @return
         */
        private OnImageLoaderListener getImageLoaderListener() {
            return imageLoaderListener;
        }

        /**
         * @param imageLoaderListener
         * @return
         */
        @CheckResult
        public Builder setImageLoaderListener(OnImageLoaderListener imageLoaderListener) {
            this.imageLoaderListener = imageLoaderListener;
            return this;
        }
    }
}
