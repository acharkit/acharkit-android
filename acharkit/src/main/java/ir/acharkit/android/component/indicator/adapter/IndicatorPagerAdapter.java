package ir.acharkit.android.component.indicator.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.component.indicator.model.IndicatorPagerModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class IndicatorPagerAdapter extends FragmentPagerAdapter {

    private List<IndicatorPagerModel> items = new ArrayList<>();

    /**
     * @param fragmentManager
     */
    public IndicatorPagerAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return items.get(position).getFragment();
    }

    /**
     * @param position
     * @return
     */
    public IndicatorPagerModel getRawItem(int position) {
        return items.get(position);
    }

    /**
     * @return
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * @param item
     */
    public void add(@NonNull IndicatorPagerModel item) {
        items.add(item);
    }

}
