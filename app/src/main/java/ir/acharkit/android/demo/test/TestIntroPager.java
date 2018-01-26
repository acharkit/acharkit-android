package ir.acharkit.android.demo.test;

import android.os.Bundle;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.IndicatorPager;
import ir.acharkit.android.component.listener.OnPageChangeListener;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.UseFragment;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class TestIntroPager extends AbstractActivity {

    private static final String TAG = TestIntroPager.class.getSimpleName();

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
        setContentView(R.layout.activity_test_intro_pager);

        UseFragment useFragment0 = new UseFragment();
        UseFragment useFragment1 = new UseFragment();
        UseFragment useFragment2 = new UseFragment();
        useFragment0.setTags("First");
        useFragment1.setTags("Second");
        useFragment2.setTags("Third");

        final IndicatorPager indicatorPager = new IndicatorPager(this, R.id.viewPager, R.id.indicator);
        indicatorPager.setOffscreenPageLimit(3);
        indicatorPager.add(useFragment0);
        indicatorPager.add(useFragment1);
        indicatorPager.add(useFragment2);

        indicatorPager.addOnPageChangeListener(new OnPageChangeListener() {
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