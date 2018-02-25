package ir.acharkit.android.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/15/18
 * Email:   alirezat775@gmail.com
 */
public interface OnImageLoaderListener {
    /**
     * @param imageView
     * @param url
     */
    void onStart(ImageView imageView, String url);

    /**
     * @param imageView
     * @param url
     * @param bitmap
     */
    void onCompleted(ImageView imageView, String url, Bitmap bitmap);

    /**
     * @param reason
     */
    void onFailure(String reason);
}
