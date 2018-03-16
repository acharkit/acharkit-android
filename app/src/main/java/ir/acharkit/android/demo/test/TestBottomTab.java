package ir.acharkit.android.demo.test;

import android.graphics.Typeface;
import android.os.Bundle;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.BottomTab;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.UseFragment;
import ir.acharkit.android.demo.fragment.UseFragmentOne;
import ir.acharkit.android.demo.fragment.UseFragmentThree;
import ir.acharkit.android.demo.fragment.UseFragmentTwo;
import ir.acharkit.android.util.Color;

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

        BottomTab bottomTab = new BottomTab(this, R.id.frameLayout, R.id.bottom_tab);

        UseFragmentOne useFragment0 = new UseFragmentOne();
        UseFragmentTwo useFragment1 = new UseFragmentTwo();
        UseFragmentThree useFragment2 = new UseFragmentThree();
        useFragment0.setTags("First");
        useFragment1.setTags("Second");
        useFragment2.setTags("Third");

        bottomTab.setEnableType(BottomTab.EnableType.TEXT_ANIMATION);
        bottomTab.setFont("OpenSans.ttf", Typeface.BOLD_ITALIC);
        bottomTab.setBackground(Color.LTGRAY);
        bottomTab.setTabItemColor(Color.MAGENTA, Color.WHITE);
        bottomTab.add(useFragment0, "test", android.R.drawable.stat_notify_more);
        bottomTab.add(useFragment1, "test", android.R.drawable.stat_notify_chat);
        bottomTab.add(useFragment2, "test", android.R.drawable.stat_notify_sync);
        bottomTab.setDefaultTab(1);

    }
}
