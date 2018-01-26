package ir.acharkit.android.connection;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    07/11/2017
 * Email:   alirezat775@gmail.com
 */
public interface OnRequestListener {
    void success(String response);
    void error(String error);
}
