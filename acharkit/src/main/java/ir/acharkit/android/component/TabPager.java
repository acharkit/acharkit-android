package ir.acharkit.android.component;


import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ir.acharkit.android.component.tabPager.TabPagerView;
import ir.acharkit.android.component.tabPager.adapter.TabPagerAdapter;
import ir.acharkit.android.component.tabPager.model.TabPagerModel;
import ir.acharkit.android.util.helper.ConvertHelper;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class TabPager {

    private static final String TAG = TabPager.class.getName();
    private ViewPager viewPager;
    private TabPagerView tabLayout;
    private TabPagerAdapter adapter;
    private AppCompatActivity activity;
    private ViewPager.OnPageChangeListener changeListener;

    /**
     * @param activity    current context, will be used to access resources
     * @param view        root view (return inflater view for example use in fragment in onCreateView)
     * @param viewPagerId id of viewPager
     * @param tabLayoutId id of tabLayout
     */
    public TabPager(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int viewPagerId, @IdRes int tabLayoutId) {
        init(activity, view, viewPagerId, tabLayoutId);
    }

    /**
     * @param activity    current context, will be used to access resources
     * @param viewPagerId id of viewPager
     * @param tabLayoutId id of tabLayout
     */
    public TabPager(@NonNull AppCompatActivity activity, @IdRes int viewPagerId, @IdRes int tabLayoutId) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, viewPagerId, tabLayoutId);
    }

    /**
     * @param activity    current context, will be used to access resources
     * @param view        root view
     * @param viewPagerId id of viewPager
     * @param tabLayoutId id of tabLayout
     */
    private void init(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int viewPagerId, @IdRes int tabLayoutId) {
        this.activity = activity;
        viewPager = view.findViewById(viewPagerId);
        tabLayout = view.findViewById(tabLayoutId);
        adapter = new TabPagerAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * @param listener change listener viewPager
     */
    public void addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        changeListener = listener;
        viewPager.addOnPageChangeListener(listener);
    }

    /**
     * refresh icons after selected
     */
    private void refreshIcons() {
        for (int i = 0; i < adapter.getCount(); i++) {
            int itemIcon = adapter.getRawItem(i).getIcon();
            if (itemIcon != 0) {
                tabLayout.getTabAt(i).setIcon(adapter.getRawItem(i).getIcon());
            }

        }
    }

    /*
        tabViews.add(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i));
        Logger.d(TAG, tabViews.toString() + "\n" + tabViews.size());

 */


    /**
     * @param fragmentClass fragment for show content
     * @param title         text such as title tabLayout
     * @param icon          image such as icon tabLayout
     */
    public void add(@NonNull Fragment fragmentClass, String title, int icon) {
        TabPagerModel item = new TabPagerModel(fragmentClass, title, icon);
        adapter.add(item);
        adapter.notifyDataSetChanged();
        refreshIcons();
    }

    /**
     * @param fragmentClass fragment for show content
     * @param title         text such as title tabLayout
     */
    public void add(Fragment fragmentClass, String title) {
        add(fragmentClass, title, 0);
    }

    /**
     * @param fragmentClass fragment for show content
     * @param icon          image such as icon tabLayout
     */
    public void add(Fragment fragmentClass, @DrawableRes int icon) {
        add(fragmentClass, null, icon);
    }

    /**
     * @param index position view
     * @param icon  image icon
     */
    public void setIcon(int index, @DrawableRes int icon) {
        adapter.getRawItem(index).setIcon(icon);
        refreshIcons();
    }

    /**
     * @param index position view
     * @param title text title
     */
    public void setTitle(int index, String title) {
        adapter.getRawItem(index).setTitle(title);
        adapter.notifyDataSetChanged();
        refreshIcons();
    }

    /**
     * @param index           add badge to index of bottomTab
     * @param backgroundColor change badge backgroundColor
     * @param textColor       change badge textColor
     * @param number          change badge number
     */
    public void addBadge(int index, @ColorInt int backgroundColor, @ColorInt int textColor, int number) {
        tabLayout.addBadge(index, backgroundColor, textColor, number);
    }

    /**
     * @param index remove badge from index of bottomTab
     */
    public void removeBadge(int index) {
        tabLayout.removeBadge(index);
    }

    /**
     * @param pathFont font name from fonts folder in assets
     * @param typeface type of typeface like BOLD, NORMAL
     */
    public void setFont(@NonNull String pathFont, int typeface) {
        tabLayout.setFont(pathFont, typeface);
    }

    /**
     * @param pageLimit How many pages will be kept offscreen in an idle state
     */
    public void setOffscreenPageLimit(int pageLimit) {
        viewPager.setOffscreenPageLimit(pageLimit);
    }

    /**
     * @param color change color indicator
     */
    public void setIndicatorColor(@ColorInt int color) {
        tabLayout.setSelectedTabIndicatorColor(color);
    }

    /**
     * @param height change height tabLayout
     */
    public void setIndicatorHeight(int height) {
        tabLayout.setSelectedTabIndicatorHeight(ConvertHelper.dpToPx(activity, height));
    }

    /**
     * text colors for the different states (normal, selected) used for the tabs
     *
     * @param inActiveColor
     * @param activeColor
     */
    public void setTabItemColor(@ColorInt int inActiveColor, @ColorInt int activeColor) {
        tabLayout.setTabTextColors(inActiveColor, activeColor);
    }
}
