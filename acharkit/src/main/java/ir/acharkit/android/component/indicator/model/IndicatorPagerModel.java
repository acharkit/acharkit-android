package ir.acharkit.android.component.indicator.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class IndicatorPagerModel {

    private Fragment fragment;

    /**
     * @param fragment
     */
    public IndicatorPagerModel(@NonNull Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * @return
     */
    public Fragment getFragment() {
        return fragment;
    }

    /**
     * @param fragment
     */
    public void setFragment(@NonNull Fragment fragment) {
        this.fragment = fragment;
    }

}
