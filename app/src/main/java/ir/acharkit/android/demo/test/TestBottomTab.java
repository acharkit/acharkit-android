package ir.acharkit.android.demo.test;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.app.AbstractFragment;
import ir.acharkit.android.component.BottomTab;
import ir.acharkit.android.component.bottomTab.OnTabChangeListener;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.fragment.UseFragmentOne;
import ir.acharkit.android.demo.fragment.UseFragmentThree;
import ir.acharkit.android.demo.fragment.UseFragmentTwo;
import ir.acharkit.android.util.Colour;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/10/18
 * Email:   alirezat775@gmail.com
 */
public class TestBottomTab extends AbstractActivity {

    private static final String TAG = TestBottomTab.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bottom_tab);

        final BottomTab bottomTab = new BottomTab(this, R.id.frameLayout, R.id.bottom_tab);

        UseFragmentOne useFragment0 = new UseFragmentOne();
        UseFragmentTwo useFragment1 = new UseFragmentTwo();
        UseFragmentThree useFragment2 = new UseFragmentThree();
        useFragment0.setTags("First");
        useFragment1.setTags("Second");
        useFragment2.setTags("Third");

        bottomTab.setTabItemColor(Colour.BLUE, Colour.RED);
        bottomTab.setEnableType(BottomTab.EnableType.TRANSLATION_ANIMATION);
        bottomTab.setFont("OpenSans.ttf", Typeface.NORMAL);
        bottomTab.setBackground(Colour.LTGRAY);
        bottomTab.add(useFragment0, "test", android.R.drawable.stat_notify_more);
        bottomTab.add(useFragment1, "test", android.R.drawable.stat_notify_chat);
        bottomTab.add(useFragment2, "test", android.R.drawable.stat_notify_sync);
        bottomTab.setDefaultTab(1);

        bottomTab.addBadge(1, Colour.MAGENTA, Colour.WHITE, 78);

        bottomTab.setTabChangeListener(new OnTabChangeListener() {
            @Override
            public void tabChange(int index) {
                if (index == 2)
                    bottomTab.removeBadge(1);
                if (index == 0)
                    bottomTab.addBadge(1, Colour.MAGENTA, Colour.WHITE, 48);

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomTab.hide();
            }
        }, 10000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomTab.show();
            }
        }, 20000);

    }

    public void presentFragment(AbstractFragment fragment, boolean addToBackStack) {
        fragment.setTagId(fragment.getClass().getSimpleName());
        fragment.actionFragment(R.id.frameLayout, AbstractFragment.TYPE_REPLACE, addToBackStack);
    }
}
