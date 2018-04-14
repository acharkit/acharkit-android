package ir.acharkit.android.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.acharkit.android.app.AbstractFragment;
import ir.acharkit.android.component.BottomTab;
import ir.acharkit.android.demo.R;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class UseFragmentTwo extends AbstractFragment {

    private View layout;
    private TextView test_fragment;
    private String tags;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_use, container, false);
        test_fragment = layout.findViewById(R.id.test_fragment);
        test_fragment.setText(getTags());

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomTab.getBottomTab().setChangeResume(1);
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
