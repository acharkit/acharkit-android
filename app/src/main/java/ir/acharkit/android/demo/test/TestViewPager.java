package ir.acharkit.android.demo.test;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.TabPager;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.UseFragment;
import ir.acharkit.android.util.Colour;
import ir.acharkit.android.util.Logger;

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
        tab.setIndicatorColor(Colour.BLUE);
        tab.setIndicatorHeight(4);
        tab.setTabItemColor(Colour.BLUE, Colour.RED);

        tab.add(useFragment0, "One", R.mipmap.ic_launcher);
        tab.add(useFragment1, "Two", R.mipmap.ic_launcher);
        tab.add(useFragment2, "Three", R.mipmap.ic_launcher);
        tab.setFont("OpenSans.ttf", Typeface.NORMAL);
        tab.addBadge(1, Colour.RED, Colour.WHITE, 1);

        tab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logger.i(TAG, "position: " + position);
                Logger.i(TAG, "positionOffset: " + positionOffset);
                Logger.i(TAG, "positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Logger.i(TAG, "onPageSelected : position: " + position);
                if (position == 2)
                    tab.removeBadge(1);
                if (position == 0)
                    tab.addBadge(1, Colour.RED, Colour.WHITE, 96);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Logger.i(TAG, "state: " + state);
            }
        });
    }
}