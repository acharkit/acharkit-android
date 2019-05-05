package ir.acharkit.android.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import ir.acharkit.android.component.carousel.CarouselView;
import ir.acharkit.android.component.roster.RosterView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */


@IntDef({RosterView.VERTICAL, RosterView.HORIZONTAL})
@Retention(RetentionPolicy.SOURCE)
public @interface RosterOrientation {
}