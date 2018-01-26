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

public class AbstractFragment extends Fragment {

    public static final int TYPE_REPLACE = 0;
    public static final int TYPE_ADD = 1;
    private static final String TAG = AbstractFragment.class.getName();
    private FragmentTransaction fragmentTransaction;
    private String tagId;

    public AbstractFragment() {
    }

    /**
     * @return
     */
    public String getTagId() {
        if (tagId != null)
            return tagId;
        else
            return getClass().getName();
    }

    /**
     * @param tagId
     */
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * @return
     */
    public FragmentTransaction getFragmentTransaction() {
        if (fragmentTransaction == null) {
            if (AbstractActivity.getActivity() == null)
                throw new NullPointerException("Your activity must extends from AbstractActivity");
            else
                fragmentTransaction = AbstractActivity.getActivity().getSupportFragmentManager().beginTransaction();
        }
        return fragmentTransaction;
    }

    /**
     * @param frameLayoutId
     * @param type
     */
    public void actionFragment(@IdRes int frameLayoutId, @FragmentStack int type, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentTransaction();
        switch (type) {
            case TYPE_REPLACE:
                transaction.replace(frameLayoutId, findFragment() != null ? findFragment() : this, getTagId());
                break;
            case TYPE_ADD:
                transaction.add(frameLayoutId, findFragment() != null ? findFragment() : this, getTagId());
                break;
        }
        if (addToBackStack) {
            transaction.addToBackStack(getTagId());
        }
        transaction.commit();
    }

    /**
     * @return
     */
    private AbstractFragment findFragment() {
        return AbstractActivity.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0 ? (AbstractFragment) AbstractActivity.getActivity().getSupportFragmentManager().findFragmentByTag(getTagId()) : null;
    }

    /**
     *
     */
    public void removeFragmentPopBackStack() {
        AbstractActivity.getActivity().getSupportFragmentManager().popBackStack(getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
