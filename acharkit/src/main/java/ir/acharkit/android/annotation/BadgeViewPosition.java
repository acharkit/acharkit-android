package ir.acharkit.android.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ir.acharkit.android.component.badge.BadgeView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */


@IntDef({BadgeView.POSITION_TOP_LEFT, BadgeView.POSITION_TOP_RIGHT, BadgeView.POSITION_BOTTOM_LEFT, BadgeView.POSITION_BOTTOM_RIGHT, BadgeView.POSITION_CENTER, BadgeView.POSITION_CENTER_TOP, BadgeView.POSITION_CENTER_BOTTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface BadgeViewPosition {
}