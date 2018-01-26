package ir.acharkit.android.annotation;

import android.support.annotation.IntDef;
import android.view.Gravity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */

@IntDef({Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL, Gravity.END, Gravity.BOTTOM, Gravity.TOP, Gravity.START})
@Retention(RetentionPolicy.SOURCE)
public @interface ViewGravity {
}
