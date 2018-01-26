package ir.acharkit.android.component.tabLayout.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class TabLayoutModel {

    private Fragment fragment;
    private String title;
    private int icon;

    /**
     * @param fragment
     * @param title
     * @param icon
     */
    public TabLayoutModel(@NonNull Fragment fragment, @NonNull String title, @DrawableRes int icon) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
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

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public int getIcon() {
        return icon;
    }

    /**
     * @param icon
     */
    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

}
