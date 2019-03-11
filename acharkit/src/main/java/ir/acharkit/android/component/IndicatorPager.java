package ir.acharkit.android.component;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import ir.acharkit.android.component.indicator.OnPageChangeListener;
import ir.acharkit.android.component.indicator.ViewPagerIndicator;
import ir.acharkit.android.component.indicator.adapter.IndicatorPagerAdapter;
import ir.acharkit.android.component.indicator.model.IndicatorPagerModel;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class IndicatorPager {

    private ViewPager viewPager;
    private ViewPagerIndicator indicator;
    private IndicatorPagerAdapter adapter;
    private AppCompatActivity activity;
    private OnPageChangeListener changeListener;

    /**
     * @param activity
     * @param view
     * @param viewPagerId
     * @param viewPagerIndicator
     */
    public IndicatorPager(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int viewPagerId, @IdRes int viewPagerIndicator) {
        init(activity, view, viewPagerId, viewPagerIndicator);
    }

    /**
     * @param activity
     * @param viewPagerId
     * @param viewPagerIndicator
     */
    public IndicatorPager(@NonNull AppCompatActivity activity, @IdRes int viewPagerId, @IdRes int viewPagerIndicator) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, viewPagerId, viewPagerIndicator);
    }

    /**
     * @param activity
     * @param view
     * @param viewPagerId
     * @param viewPagerIndicator
     */
    private void init(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int viewPagerId, @IdRes int viewPagerIndicator) {
        this.activity = activity;
        viewPager = view.findViewById(viewPagerId);
        indicator = view.findViewById(viewPagerIndicator);
        adapter = new IndicatorPagerAdapter(activity.getSupportFragmentManager());

        if (indicator == null)
            throw new NullPointerException("indicator is Null");

        if (viewPager == null)
            throw new NullPointerException("viewPager is Null");
        else
            viewPager.setAdapter(adapter);
    }

    /**
     * @param listener
     */
    public void addOnPageChangeListener(final OnPageChangeListener listener) {
        changeListener = listener;
        if (changeListener != null)
            indicator.addOnPageChangeListener(changeListener);
        refreshIndicator();
    }

    /**
     *
     */
    private void refreshIndicator() {
        indicator.setupWithViewPager(viewPager);
    }


    /**
     * @param fragmentClass
     */
    public void add(@NonNull Fragment fragmentClass) {
        IndicatorPagerModel item = new IndicatorPagerModel(fragmentClass);
        adapter.add(item);
        adapter.notifyDataSetChanged();
        refreshIndicator();
    }

    /**
     * @param pageLimit
     */
    public void setOffscreenPageLimit(int pageLimit) {
        viewPager.setOffscreenPageLimit(pageLimit);
    }


}
