package ir.acharkit.android.imageLoader;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import ir.acharkit.android.imageLoader.cache.DiskCache;
import ir.acharkit.android.imageLoader.cache.MemoryCache;
import ir.acharkit.android.imageLoader.cache.OnCacheListener;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/2/18
 * Email:   alirezat775@gmail.com
 */

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getName();
    public static MemoryCache memoryCache = new MemoryCache();
    private OnImageLoaderListener imageLoaderListener;
    private DiskCache diskCache;
    private Context context;
    private int placeHolder;

    private ImageLoader(Builder builder) {
        this.context = builder.context;
        this.diskCache = builder.diskCache;
        this.placeHolder = builder.placeHolder;
        this.imageLoaderListener = builder.imageLoaderListener;
    }

    public void load(final ImageView imageView, final String url) {
        if (imageLoaderListener != null) {
            imageLoaderListener.onStart(imageView, url);
        }

        if (placeHolder != 0) imageView.setImageResource(placeHolder);

        ImageLoaderManager manager = new ImageLoaderManager(context, imageView);
        manager.setMemoryCache(memoryCache);
        manager.setDiskCache(diskCache);
        manager.setListener(imageLoaderListener);
        manager.load(url);
    }

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

    public void clearCache() {
            ImageLoaderManager.imageViewList.clear();
            memoryCache.clear();
            diskCache.clear(null);
    }

    public static class Builder {

        private Context context;
        private DiskCache diskCache;
        private int placeHolder;
        private OnImageLoaderListener imageLoaderListener;

        /**
         * @param context the best practice is passing application context
         * @param downloadDir for setting custom download directory  (default value is sandbox/download/ directory)
         */
        public Builder(@NonNull Context context, String downloadDir) {
            this.context = context;
            diskCache = DiskCache.getInstance(context, downloadDir);
        }

        /**
         * @param imageLoaderListener callback event action on imageLoader
         * @return
         */
        @CheckResult
        public Builder setImageLoaderListener(OnImageLoaderListener imageLoaderListener) {
            this.imageLoaderListener = imageLoaderListener;
            return this;
        }

        /**
         * @param placeHolder load image before loading real image
         */
        @CheckResult
        public Builder setPlaceHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }

    }
}
