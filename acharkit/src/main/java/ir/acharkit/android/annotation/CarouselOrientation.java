package ir.acharkit.android.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ir.acharkit.android.component.carousel.CarouselView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */


@IntDef({CarouselView.VERTICAL, CarouselView.HORIZONTAL})
@Retention(RetentionPolicy.SOURCE)
public @interface CarouselOrientation {
}