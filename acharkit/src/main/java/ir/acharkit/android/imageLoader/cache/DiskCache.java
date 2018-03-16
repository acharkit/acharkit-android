package ir.acharkit.android.imageLoader.cache;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import ir.acharkit.android.imageLoader.ImageLoaderUtil;
import ir.acharkit.android.util.helper.MimeHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/2/18
 * Email:   alirezat775@gmail.com
 */

public class DiskCache {

    private static DiskCache instance;
    private File cacheDir;
    private String dir;

    /**
     * @param dir
     */
    DiskCache(Context context, String dir) {
        if (dir == null || dir.isEmpty()) {
            dir = (String.valueOf(context.getExternalFilesDir("imageLoader")));
        }
        this.dir = dir;
        cacheDir = new File(dir);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    /**
     * @param dir
     * @return
     */
    public static DiskCache getInstance(Context context, String dir) {
        if (instance == null)
            instance = new DiskCache(context, dir);
        return instance;
    }

    /**
     * @param listener
     */
    public void clear(CacheClear listener) {
        if (cacheDir.isDirectory()) {
            String[] children = cacheDir.list();
            for (int i = 0; i < children.length; i++) {
                new File(cacheDir, children[i]).delete();
            }
        }
        if (listener != null)
            listener.clear();
    }

    /**
     * @param url
     * @param extension
     * @return
     */
    public File getFile(String url, String extension) {
        return new File(cacheDir, ImageLoaderUtil.getHashName(url) + "." + extension);
    }

    /**
     * @return cache directory
     */
    public String getCacheDir() {
        return dir;
    }

    /**
     * @param url image url
     * @throws IOException
     */
    public void put(String url) throws IOException {
        String extension = MimeHelper.getFileExtensionFromUrl(url);
        File file = new File(getCacheDir(), ImageLoaderUtil.getHashName(url) + "." + extension);
        ImageLoaderUtil.copyInputStreamToFile(new URL(url).openConnection().getInputStream(), file);
    }

    /**
     *
     */
    public interface CacheClear {
        void clear();
    }
}
