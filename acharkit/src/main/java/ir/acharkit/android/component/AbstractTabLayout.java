package ir.acharkit.android.component;


import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ir.acharkit.android.component.tabLayout.adapter.TabLayoutAdapter;
import ir.acharkit.android.component.tabLayout.model.TabLayoutModel;
import ir.acharkit.android.util.helper.ConvertHelper;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class AbstractTabLayout {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabLayoutAdapter adapter;
    private AppCompatActivity activity;
    private ViewPager.OnPageChangeListener changeListener;

    /**
     * @param activity
     * @param view
     * @param viewPagerId
     * @param tabLayoutId
     */
    public AbstractTabLayout(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int viewPagerId, @IdRes int tabLayoutId) {
        init(activity, view, viewPagerId, tabLayoutId);
    }

    /**
     * @param activity
     * @param viewPagerId
     * @param tabLayoutId
     */
    public AbstractTabLayout(@NonNull AppCompatActivity activity, @IdRes int viewPagerId, @IdRes int tabLayoutId) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, viewPagerId, tabLayoutId);
    }

    /**
     * @param activity
     * @param view
     * @param viewPagerId
     * @param tabLayoutId
     */
    private void init(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int viewPagerId, @IdRes int tabLayoutId) {
        this.activity = activity;
        viewPager = view.findViewById(viewPagerId);
        tabLayout = view.findViewById(tabLayoutId);
        adapter = new TabLayoutAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * @param listener
     */
    public void addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        changeListener = listener;
        viewPager.addOnPageChangeListener(listener);
    }

    /**
     *
     */
    private void refreshIcons() {
        for (int i = 0; i < adapter.getCount(); i++) {
            int itemIcon = adapter.getRawItem(i).getIcon();
            if (itemIcon != 0) {
                tabLayout.getTabAt(i).setIcon(adapter.getRawItem(i).getIcon());
            }
        }
    }

    /**
     * @param fragmentClass
     * @param title
     * @param icon
     */
    public void add(@NonNull Fragment fragmentClass, String title, int icon) {
        TabLayoutModel item = new TabLayoutModel(fragmentClass, title, icon);
        adapter.add(item);
        adapter.notifyDataSetChanged();
        refreshIcons();
    }

    /**
     * @param fragmentClass
     * @param title
     */
    public void add(Fragment fragmentClass, String title) {
        add(fragmentClass, title, 0);
    }

    /**
     * @param fragmentClass
     * @param icon
     */
    public void add(Fragment fragmentClass, @DrawableRes int icon) {
        add(fragmentClass, null, icon);
    }

    /**
     * @param index
     * @param icon
     */
    public void setIcon(int index, @DrawableRes int icon) {
        adapter.getRawItem(index).setIcon(icon);
        refreshIcons();
    }

    /**
     * @param index
     * @param title
     */
    public void setTitle(int index, String title) {
        adapter.getRawItem(index).setTitle(title);
        adapter.notifyDataSetChanged();
        refreshIcons();
    }

    /**
     * @param pageLimit
     */
    public void setOffscreenPageLimit(int pageLimit) {
        viewPager.setOffscreenPageLimit(pageLimit);
    }

    /**
     * @param color
     */
    public void setIndicatorColor(@ColorInt int color) {
        tabLayout.setSelectedTabIndicatorColor(color);
    }

    /**
     * @param height
     */
    public void setIndicatorHeight(int height) {
        tabLayout.setSelectedTabIndicatorHeight(ConvertHelper.dpToPx(activity, height));
    }

    /**
     * @param inActiveColor
     * @param activeColor
     */
    public void setTabItemColor(@ColorInt int inActiveColor, @ColorInt int activeColor) {
        tabLayout.setTabTextColors(inActiveColor, activeColor);
    }
}
