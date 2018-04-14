package ir.acharkit.android.component;

import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.app.AbstractFragment;
import ir.acharkit.android.component.bottomTab.BottomTabView;
import ir.acharkit.android.component.bottomTab.OnTabChangeListener;
import ir.acharkit.android.component.bottomTab.model.BottomTabModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/10/18
 * Email:   alirezat775@gmail.com
 */
public class BottomTab {

    private static final String TAG = BottomTab.class.getName();
    private static final int ANIMATION_DURATION = 300;
    private static BottomTab bottomTab;
    private ArrayList<BottomTabModel> tabList = new ArrayList<>();
    private AbstractActivity activity;
    private BottomTabView bottomTabView;
    private int container;
    private boolean mainTab = false;
    private OnTabChangeListener tabChangeListener;

    /**
     * @param activity
     * @param frameLayoutId
     * @param bottomTabId
     */
    public BottomTab(AbstractActivity activity, @IdRes int frameLayoutId, @IdRes int bottomTabId) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, frameLayoutId, bottomTabId);
    }

    /**
     * @param activity
     * @param view
     * @param frameLayoutId
     * @param bottomTabId
     */
    public BottomTab(AbstractActivity activity, View view, @IdRes int frameLayoutId, @IdRes int bottomTabId) {
        init(activity, view, frameLayoutId, bottomTabId);
    }

    /**
     * @return instance of BottomTab
     */
    public static BottomTab getBottomTab() {
        if (bottomTab == null)
            throw new NullPointerException("You must be create instance from BottomTab");
        return bottomTab;
    }

    /**
     * @param activity
     * @param view
     * @param frameLayoutId
     * @param bottomTabId
     */
    private void init(final AbstractActivity activity, View view, @IdRes final int frameLayoutId, @IdRes int bottomTabId) {
        this.activity = activity;
        container = frameLayoutId;
        bottomTabView = view.findViewById(bottomTabId);
        bottomTabView.setTabChangeListener(new BottomTabView.TabChangeListener() {
            @Override
            public void onTabChanged(int index) {
                if (getTabChangeListener() != null) getTabChangeListener().tabChange(index);
                AbstractFragment fragment = tabList.get(index).getFragment();
                presentTabFragment(fragment, container);
            }
        });
        bottomTab = this;
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

    public void setChangeResume(int index) {
        bottomTabView.setSelected(index);
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
        fragment.actionFragment(container, AbstractFragment.TYPE_REPLACE, mainTab);
        mainTab = true;
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
     * show BottomTabView
     */
    public void show() {
        if (bottomTabView.getVisibility() == View.VISIBLE)
            return;
        TranslateAnimation animate = new TranslateAnimation(0, 0, bottomTabView.getHeight(), 0);
        animate.setDuration(ANIMATION_DURATION);
        bottomTabView.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomTabView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * hide BottomTabView
     */
    public void hide() {
        if (bottomTabView.getVisibility() == View.GONE)
            return;
        bottomTabView.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, bottomTabView.getHeight());
        animate.setDuration(ANIMATION_DURATION);
        bottomTabView.startAnimation(animate);
    }

    /**
     * @return TabChangeListener
     */
    private OnTabChangeListener getTabChangeListener() {
        return tabChangeListener;
    }

    /**
     * @param tabChangeListener the new listener to set, or null to set no listener
     */
    public void setTabChangeListener(OnTabChangeListener tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
    }

    /**
     * @param index           add badge to index of bottomTab
     * @param backgroundColor change badge backgroundColor
     * @param textColor       change badge textColor
     * @param number          change badge number
     */
    public void addBadge(int index, int backgroundColor, int textColor, int number) {
        bottomTabView.addBadge(index, backgroundColor, textColor, number);
    }

    /**
     * @param index remove badge from index of bottomTab
     */
    public void removeBadge(int index) {
        bottomTabView.removeBadge(index);
    }

    /**
     * type of animation
     */
    public enum EnableType {
        TRANSLATION_ANIMATION,
        NO_ANIMATION,
        ALPHA_ANIMATION,
    }

}
