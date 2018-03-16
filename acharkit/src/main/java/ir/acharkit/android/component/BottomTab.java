package ir.acharkit.android.component;

import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import ir.acharkit.android.app.AbstractFragment;
import ir.acharkit.android.component.bottomTab.BottomTabView;
import ir.acharkit.android.component.bottomTab.model.BottomTabModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/10/18
 * Email:   alirezat775@gmail.com
 */
public class BottomTab {

    private static final String TAG = BottomTab.class.getName();
    private ArrayList<BottomTabModel> tabList = new ArrayList<>();
    private AppCompatActivity activity;
    private BottomTabView bottomTabView;
    private int container;

    /**
     * @param activity
     * @param frameLayoutId
     * @param bottomTabId
     */
    public BottomTab(AppCompatActivity activity, @IdRes int frameLayoutId, @IdRes int bottomTabId) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, frameLayoutId, bottomTabId);
    }

    /**
     * @param activity
     * @param view
     * @param frameLayoutId
     * @param bottomTabId
     */
    public BottomTab(AppCompatActivity activity, View view, @IdRes int frameLayoutId, @IdRes int bottomTabId) {
        init(activity, view, frameLayoutId, bottomTabId);
    }

    /**
     * @param activity
     * @param view
     * @param frameLayoutId
     * @param bottomTabId
     */
    private void init(AppCompatActivity activity, View view, @IdRes final int frameLayoutId, @IdRes int bottomTabId) {
        this.activity = activity;
        container = frameLayoutId;
        bottomTabView = view.findViewById(bottomTabId);
        bottomTabView.setTabChangeListener(new BottomTabView.TabChangeListener() {
            @Override
            public void onTabChanged(int index) {
                AbstractFragment fragment = tabList.get(index).getFragment();
                presentTabFragment(fragment, container);
            }
        });
    }

    /**
     * @param pathFont font name from fonts folder in assets
     * @param typeface type of typeface like BOLD, NORMAL
     */
    public void setFont(@NonNull String pathFont, int typeface) {
        bottomTabView.setFont(pathFont, typeface);
    }

    /**
     * @param fragmentClass fragment for show when tab selected
     * @param icon          tab icon
     */
    public void add(@NonNull AbstractFragment fragmentClass, int icon) {
        add(fragmentClass, null, icon);
    }

    /**
     * @param fragmentClass fragment for show when tab selected
     * @param title         tab text
     * @param icon          tab icon
     */
    public void add(@NonNull AbstractFragment fragmentClass, String title, int icon) {
        BottomTabModel tabModel = new BottomTabModel();
        tabModel.setFragment(fragmentClass);
        tabModel.setTitle(title);
        tabModel.setIcon(icon);
        bottomTabView.addItem(title, icon);
        tabList.add(tabModel);
    }

    /**
     * @param tabChangeListener
     */
    private void setTabChangeListener(BottomTabView.TabChangeListener tabChangeListener) {
        bottomTabView.setTabChangeListener(tabChangeListener);
    }

    /**
     * @param type set animation type
     */
    public void setEnableType(EnableType type) {
        bottomTabView.setEnableType(type);
    }

    /**
     * @param defaultTab change default tab
     */
    public void setDefaultTab(final int defaultTab) {
        bottomTabView.post(new Runnable() {
            @Override
            public void run() {
                bottomTabView.setSelected(defaultTab);
                AbstractFragment fragment = tabList.get(defaultTab).getFragment();
                presentTabFragment(fragment, container);
            }
        });
    }

    /**
     * @param color change color background
     */
    public void setBackground(@ColorInt final int color) {
        bottomTabView.post(new Runnable() {
            @Override
            public void run() {
                bottomTabView.setBackgroundColor(color);
            }
        });
    }

    /**
     * show fragment with action in container layout
     *
     * @param fragment  instance of AbstractFragment
     * @param container layout id
     */
    private void presentTabFragment(@NonNull AbstractFragment fragment, @IdRes int container) {
        fragment.actionFragment(container, AbstractFragment.TYPE_REPLACE, true);
    }

    /**
     * @param activeColor   change color when tab selected
     * @param inActiveColor change color when tab unSelected
     */
    public void setTabItemColor(int activeColor, int inActiveColor) {
        bottomTabView.setActiveColor(activeColor);
        bottomTabView.setInActiveColor(inActiveColor);
    }

    /**
     * type of animation
     */
    public enum EnableType {
        TEXT_ANIMATION,
        COLOR_ANIMATION,
        ALPHA_ANIMATION,
    }
}
