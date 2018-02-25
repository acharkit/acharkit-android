package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.TabPager;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.UseFragment;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class TestViewPager extends AbstractActivity {

    private static final String TAG = TestViewPager.class.getSimpleName();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!hasBackStack()) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_viewpager);

        UseFragment useFragment0 = new UseFragment();
        UseFragment useFragment1 = new UseFragment();
        UseFragment useFragment2 = new UseFragment();
        useFragment0.setTags("First");
        useFragment1.setTags("Second");
        useFragment2.setTags("Third");

        final TabPager tab = new TabPager(this, R.id.viewPager, R.id.tabLayout);
        tab.setOffscreenPageLimit(3);
        tab.setIndicatorColor(0xffffdd33);
        tab.setIndicatorHeight(4);
        tab.setTabItemColor(0xffff0033, 0xff00ee11);

        tab.add(useFragment0, "One", R.mipmap.ic_launcher);
        tab.add(useFragment1, "Two", R.mipmap.ic_launcher);
        tab.add(useFragment2, "Three", R.mipmap.ic_launcher);
        Log.d(TAG, "getVisibleFragment:" + getFragmentList());

        tab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(TAG, "position: " + position);
                Log.i(TAG, "positionOffset: " + positionOffset);
                Log.i(TAG, "positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected : position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "state: " + state);
            }
        });
    }
}