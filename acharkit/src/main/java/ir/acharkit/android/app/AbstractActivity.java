package ir.acharkit.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class AbstractActivity extends AppCompatActivity {

    private static final String TAG = AbstractActivity.class.getName();
    private static AbstractActivity instance;
    public HashMap<Fragment, String> fragmentList = new HashMap<>();

    /**
     * @return
     */
    public static AbstractActivity getActivity() {
        return instance;
    }

    public void startActivity(Class aClass) {
        Intent intent = new Intent(instance, aClass);
        startActivity(intent);
    }

    public Fragment findFragment(String tagId) {
        return hasBackStack() ? getSupportFragmentManager().findFragmentByTag(tagId) : null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    /**
     * @return
     */
    public boolean hasBackStack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        return count != 0;
    }

    /**
     * @return
     */
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void clearBackStack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
