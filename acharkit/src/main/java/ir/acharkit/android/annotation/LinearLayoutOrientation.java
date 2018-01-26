package ir.acharkit.android.annotation;

import android.support.annotation.IntDef;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */


@IntDef({LinearLayout.VERTICAL, LinearLayout.HORIZONTAL})
@Retention(RetentionPolicy.SOURCE)
public @interface LinearLayoutOrientation {
}