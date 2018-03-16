package ir.acharkit.android.app;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ir.acharkit.android.annotation.FragmentStack;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public abstract class AbstractFragment extends Fragment {

    public static final int TYPE_REPLACE = 0;
    public static final int TYPE_ADD = 1;
    private static final String TAG = AbstractFragment.class.getName();
    private FragmentTransaction fragmentTransaction;
    private String tagId;
    private boolean instantiate = true;

    public AbstractFragment() {
    }

    /**
     * @return identifier fragment
     */
    public String getTagId() {
        if (tagId != null)
            return tagId;
        else
            return getClass().getName();
    }

    /**
     * @param tagId identifier fragment
     */
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * @return FragmentManager from AbstractActivity
     */
    public FragmentManager fragmentManager() {
        if (AbstractActivity.getActivity() == null)
            throw new NullPointerException("Your activity must extends from AbstractActivity");
        else
            return AbstractActivity.getActivity().getSupportFragmentManager();
    }

    /**
     * @param frameLayoutId container layout
     * @param type          action replace or add
     */
    public synchronized void actionFragment(@IdRes int frameLayoutId, @FragmentStack int type, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager().beginTransaction();
        AbstractFragment fragment;
        if (isInstantiate()) fragment = findFragment() != null ? findFragment() : this;
        else fragment = this;
        switch (type) {
            case TYPE_REPLACE:
                transaction.replace(frameLayoutId, fragment, getTagId());
                break;
            case TYPE_ADD:
                transaction.add(frameLayoutId, fragment, getTagId());
                break;
        }
        if (addToBackStack) {
            transaction.addToBackStack(getTagId());
        }
        transaction.commit();
    }

    /**
     * @return find fragment with tagId
     */
    private synchronized AbstractFragment findFragment() {
        return AbstractActivity.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0 ? (AbstractFragment) AbstractActivity.getActivity().getSupportFragmentManager().findFragmentByTag(getTagId()) : null;
    }

    /**
     * remove fragment from backStack
     */
    public synchronized void removeFragmentPopBackStack() {
        fragmentManager().popBackStack();
    }

    /**
     * remove fragment
     */
    public synchronized void removeFragment() {
        FragmentTransaction transaction = fragmentManager().beginTransaction();
        AbstractFragment fragment = findFragment() != null ? findFragment() : this;
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * @return save one instance of fragment
     */
    private boolean isInstantiate() {
        return instantiate;
    }

    /**
     * @param instantiate if set true find instance from backStack and action with this instance
     *                    else if set false remove create new instance and action with this instance
     */
    public void setInstantiate(boolean instantiate) {
        this.instantiate = instantiate;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}