package ir.acharkit.android.component.indicator.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.component.indicator.model.IndicatorPagerModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class IndicatorPagerAdapter extends FragmentStatePagerAdapter {

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
     * @return raw item model
     */
    public IndicatorPagerModel getRawItem(int position) {
        return items.get(position);
    }

    /**
     * @return count of items in the list
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * @param item add item to list
     */
    public void add(@NonNull IndicatorPagerModel item) {
        items.add(item);
    }

}
