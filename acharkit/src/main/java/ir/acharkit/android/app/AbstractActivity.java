package ir.acharkit.android.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public abstract class AbstractActivity extends AppCompatActivity {

    private static final String TAG = AbstractActivity.class.getName();
    private static AbstractActivity instance;

    /**
     * @return instance of AbstractActivity
     */
    public static AbstractActivity getActivity() {
        return instance;
    }

    /**
     * @return which fragment is visible
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

    /**
     * @return list of fragment
     */
    public List<Fragment> getFragmentList() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        return fragmentManager.getFragments();
    }

    /**
     * @param aClass start new activity
     */
    public void startActivity(Class aClass) {
        Intent intent = new Intent(getActivity(), aClass);
        startActivity(intent);
    }

    /**
     * @param tagId find fragment from backStack with tagId
     * @return backStack has instance fragment
     */
    public Fragment findFragment(String tagId) {
        return hasBackStack() ? getSupportFragmentManager().findFragmentByTag(tagId) : null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    /**
     * @return backStack has any fragment
     */
    public boolean hasBackStack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        return count != 0;
    }

    /**
     * remove all fragment from backStack
     */
    public void clearBackStack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    /**
     * show fragment with action in container layout
     * @param fragment instance of AbstractFragment
     * @param container layout id
     * @param tagId identifier fragment
     * @param addToBackStack add to backStack
     */
    public void presentFragment(@NonNull AbstractFragment fragment, @IdRes int container, @NonNull String tagId, boolean addToBackStack) {
        fragment.setTagId(tagId);
        fragment.actionFragment(container, AbstractFragment.TYPE_REPLACE, addToBackStack);
    }
}
