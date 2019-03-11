package ir.acharkit.android.annotation;

import androidx.annotation.IntDef;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */


@IntDef({Toast.LENGTH_LONG, Toast.LENGTH_SHORT})
@Retention(RetentionPolicy.SOURCE)
public @interface ToastDuration {
}
