package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.View;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.app.AbstractFragment;

import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.UseFragment;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class TestFragment extends AbstractActivity {

    private static final String TAG = TestFragment.class.getSimpleName();
    private int i = 0;
    private UseFragment useFragment;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (i != 0) {
            i--;
        }
        if (!hasBackStack()) {
            finish();
        }
        if (AbstractActivity.getActivity().getFragmentManager().getBackStackEntryCount() > 0)
            useFragment.removeFragmentPopBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);

        findViewById(R.id.add_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                useFragment = new UseFragment();
                useFragment.setTags("# : " + i);
                useFragment.setTagId("TAB_" + i);
                useFragment.actionFragment(R.id.frameLayout, AbstractFragment.TYPE_REPLACE, true);
                Log.d(TAG, "getVisibleFragment:" + getVisibleFragment());
            }
        });

    }
}