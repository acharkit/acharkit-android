package ir.acharkit.android.component.bottomTab.model;

import android.support.v4.app.Fragment;

import ir.acharkit.android.app.AbstractFragment;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/11/18
 * Email:   alirezat775@gmail.com
 */
public class BottomTabModel {

    private AbstractFragment fragment;
    private int icon;
    private String title;

    public AbstractFragment getFragment() {
        return fragment;
    }

    public void setFragment(AbstractFragment fragment) {
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
