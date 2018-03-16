package ir.acharkit.android.imageLoader.cache;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import ir.acharkit.android.imageLoader.ImageLoaderUtil;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/2/18
 * Email:   alirezat775@gmail.com
 */

public class MemoryCache {
    private static final String TAG = MemoryCache.class.getName();
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(36, 0.75f, true));
    private long size = 0;
    private long limit;

    /**
     *
     */
    public MemoryCache() {
        limit = (Runtime.getRuntime().maxMemory() / 4);
    }

    /**
     * @param url
     * @return
     */
    public Bitmap get(String url) {
        return cache.get(ImageLoaderUtil.getHashName(url));
    }

    /**
     * @param url
     * @param bitmap
     */
    public void put(String url, Bitmap bitmap) {
        String hashName = ImageLoaderUtil.getHashName(url);
        if (cache.containsKey(hashName))
            size -= getSizeInBytes(cache.get(hashName));
        cache.put(hashName, bitmap);
        size += getSizeInBytes(bitmap);
        checkSize();
    }

    /**
     *
     */
    private void checkSize() {
        if (size > limit) {
            Iterator<Map.Entry<String, Bitmap>> iterator = cache.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Bitmap> entry = iterator.next();
                size -= getSizeInBytes(entry.getValue());
                iterator.remove();
                if (size <= limit)
                    break;
            }
        }
    }

    /**
     *
     */
    public void clear() {
        cache.clear();
        size = 0;
    }

    /**
     * @param bitmap
     * @return
     */
    private long getSizeInBytes(Bitmap bitmap) {
        if (bitmap != null)
            return bitmap.getRowBytes() * bitmap.getHeight();
        else
            return 0;
    }
}
