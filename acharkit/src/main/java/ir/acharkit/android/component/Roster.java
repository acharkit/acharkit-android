package ir.acharkit.android.component;


import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ir.acharkit.android.annotation.CarouselOrientation;
import ir.acharkit.android.component.roster.RosterLayoutManager;
import ir.acharkit.android.component.roster.RosterListener;
import ir.acharkit.android.component.roster.RosterView;
import ir.acharkit.android.component.roster.adapter.RosterAdapter;
import ir.acharkit.android.component.roster.model.RosterModel;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class Roster {

    private static final String TAG = Roster.class.getName();
    private RosterView rosterView;
    private RosterAdapter adapter;
    private AppCompatActivity activity;
    private RosterLayoutManager manager;
    private int currentPosition;
    private boolean autoScroll = false;
    private boolean snapping;
    private boolean enableSlider = false;

    /**
     * @param activity
     * @param carouselViewId
     * @param rosterAdapter
     */
    public Roster(@NonNull AppCompatActivity activity, @IdRes int carouselViewId, @NonNull RosterAdapter rosterAdapter) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, carouselViewId, rosterAdapter);
    }

    /**
     * @param activity
     * @param view
     * @param carouselViewId
     * @param rosterAdapter
     */
    public Roster(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int carouselViewId, @NonNull RosterAdapter rosterAdapter) {
        init(activity, view, carouselViewId, rosterAdapter);
    }

    /**
     * @param activity
     * @param view
     * @param carouselViewId
     * @param rosterAdapter
     */
    private synchronized void init(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int carouselViewId, @NonNull RosterAdapter rosterAdapter) {
        this.activity = activity;
        this.adapter = rosterAdapter;
        rosterView = view.findViewById(carouselViewId);
        rosterView.setLayoutManager(getManager());
        rosterView.setAdapter(adapter);
        rosterView.setAutoScroll(isAutoScroll());
    }

    /**
     * @param orientation
     * @param reverseLayout
     */
    public synchronized void setOrientation(@CarouselOrientation int orientation, boolean reverseLayout) {
        manager = new RosterLayoutManager(activity, orientation, reverseLayout);
        rosterView.setLayoutManager(manager);
        int padding = 0;
        switch (orientation) {
            case RosterView.HORIZONTAL:
                padding = ViewHelper.getScreenWidth() / 4;
                rosterView.setPadding(padding, 0, padding, 0);
                break;
            case RosterView.VERTICAL:
                padding = ViewHelper.getScreenHeight() / 4;
                rosterView.setPadding(0, padding, 0, padding);
                break;
        }
    }

    /**
     * @param scaleView
     */
    public void setScaleView(boolean scaleView) {
        getManager().setScaleView(scaleView);
    }

    /**
     * @return
     */
    private synchronized RosterLayoutManager getManager() {
        if (manager == null) {
            setOrientation(RosterView.HORIZONTAL, false);
        }
        return manager;
    }

    /**
     * @param items
     */
    public void addAll(@NonNull List items) {
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }

    /**
     * @param items
     */
    public void add(@NonNull RosterModel items) {
        adapter.operation(items, RosterAdapter.ADD);
        adapter.notifyDataSetChanged();
    }

    /**
     * @param items
     */
    public void remove(@NonNull RosterModel items) {
        adapter.operation(items, RosterAdapter.REMOVE);
        adapter.notifyDataSetChanged();
    }

    /**
     *
     */
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    /**
     * @return
     */
    public int getCurrentPosition() {
        return rosterView.getCurrentPosition();
    }

    /**
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        rosterView.scrollToPosition(currentPosition);
    }

    /**
     * @param listener
     */
    public void setSnappingListener(RosterListener listener) {
        rosterView.setListener(listener);
    }

    /**
     *
     */
    public void setAutoScrollPause() {
        rosterView.setAutoScrollPause();
    }


    /**
     *
     */
    public void setAutoScrollResume() {
        rosterView.setAutoScrollResume();
    }


    /**
     * @return
     */
    public boolean isAutoScroll() {
        return autoScroll;
    }

    /**
     * @param autoScroll
     * @param delayMillis
     * @param loopMode
     */
    public void setAutoScroll(boolean autoScroll, long delayMillis, boolean loopMode) {
        this.autoScroll = autoScroll;
        rosterView.setAutoScroll(autoScroll);
        rosterView.setDelayMillis(delayMillis);
        rosterView.setLoopMode(loopMode);
    }

    /**
     * @return
     */
    private boolean isSnapping() {
        return snapping;
    }

    /**
     * @param snapping
     */
    public void setSnapping(boolean snapping) {
        this.snapping = snapping;
    }

    /**
     * @param enableSlider
     */
    public void setEnableSlider(boolean enableSlider) {
        this.enableSlider = enableSlider;
        adapter.setEnableSlider(enableSlider);
    }
}
