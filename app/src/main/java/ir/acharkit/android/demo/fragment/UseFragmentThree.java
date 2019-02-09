package ir.acharkit.android.demo.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.acharkit.android.app.AbstractFragment;
import ir.acharkit.android.component.BottomTab;
import ir.acharkit.android.component.DialogView;
import ir.acharkit.android.component.Progress;
import ir.acharkit.android.component.progress.LoadingIndicatorProgress;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.UseFragment;
import ir.acharkit.android.demo.test.TestBottomTab;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class UseFragmentThree extends AbstractFragment {

    private View layout;
    private TextView test_fragment;
    private String tags;
    private DialogView.Builder builder;

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
        test_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UseFragment().actionFragment(R.id.frameLayout, AbstractFragment.TYPE_REPLACE, true);
            }
        });
//        showDialog();
        return layout;
    }

    private void showDialog() {
        final Progress progress = new Progress(getActivity())
                .setProgress(new LoadingIndicatorProgress(getActivity()))
                .setColor(0xFFFF00FF);
        progress.load();

        builder = new DialogView.Builder(getActivity());
        builder.setBackgroundColor(0xFF232323, 8)
                .setFont("OpenSans.ttf", Typeface.NORMAL)
                .setTitle("title", 5, 0xFFFFFFFF)
                .setMessage("message", 5, 0xFFFFFFFF)
                .setProgressbar(progress)
                .setButtonsViewOrientation(LinearLayout.VERTICAL)
                .addButton("button1", 5, 0xFF0A8A12, 0xFFFFFFFF, onClickListenerOne(), Gravity.CENTER, 8)
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    private View.OnClickListener onClickListenerOne() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UseFragment fragment = new UseFragment();
                fragment.setTags("hello");
                ((TestBottomTab) getActivity()).presentFragment(fragment, true);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomTab.getBottomTab().setChangeResume(2);
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
