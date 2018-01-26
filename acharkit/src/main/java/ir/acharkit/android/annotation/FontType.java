package ir.acharkit.android.annotation;

import android.graphics.Typeface;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */


@IntDef({Typeface.BOLD, Typeface.BOLD_ITALIC, Typeface.ITALIC, Typeface.NORMAL})
@Retention(RetentionPolicy.SOURCE)
public @interface FontType {
}