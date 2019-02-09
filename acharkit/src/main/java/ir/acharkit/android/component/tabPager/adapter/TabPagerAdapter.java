package ir.acharkit.android.component.tabPager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.component.tabPager.model.TabPagerModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private List<TabPagerModel> items = new ArrayList<>();

    /**
     * @param fragmentManager
     */
    public TabPagerAdapter(@NonNull FragmentManager fragmentManager) {
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
    public TabPagerModel getRawItem(int position) {
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
    public void add(@NonNull TabPagerModel item) {
        items.add(item);
    }

    /**
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

}
