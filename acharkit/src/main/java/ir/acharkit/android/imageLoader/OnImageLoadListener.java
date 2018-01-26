package ir.acharkit.android.imageLoader;

import android.widget.ImageView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    07/11/2017
 * Email:   alirezat775@gmail.com
 */
public interface OnImageLoadListener {
    /**
     * @param imageView
     */
    void onCompleted(ImageView imageView);

    /**
     * @param reason
     */
    void onFailure(String reason);

}
