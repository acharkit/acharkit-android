package ir.acharkit.android.component;


import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ir.acharkit.android.annotation.CarouselOrientation;
import ir.acharkit.android.component.carousel.CarouselLayoutManager;
import ir.acharkit.android.component.carousel.CarouselListener;
import ir.acharkit.android.component.carousel.CarouselView;
import ir.acharkit.android.component.carousel.adapter.CarouselAdapter;
import ir.acharkit.android.component.carousel.model.CarouselModel;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class Carousel {

    private static final String TAG = Carousel.class.getName();
    private CarouselView carouselView;
    private CarouselAdapter adapter;
    private AppCompatActivity activity;
    private CarouselLayoutManager manager;
    private int currentPosition;
    private boolean autoScroll = false;
    private boolean snapping;
    private boolean enableSlider = false;

    /**
     * @param activity
     * @param carouselViewId
     * @param carouselAdapter
     */
    public Carousel(@NonNull AppCompatActivity activity, @IdRes int carouselViewId, @NonNull CarouselAdapter carouselAdapter) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, carouselViewId, carouselAdapter);
    }

    /**
     * @param activity
     * @param view
     * @param carouselViewId
     * @param carouselAdapter
     */
    public Carousel(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int carouselViewId, @NonNull CarouselAdapter carouselAdapter) {
        init(activity, view, carouselViewId, carouselAdapter);
    }

    /**
     * @param activity
     * @param view
     * @param carouselViewId
     * @param carouselAdapter
     */
    private synchronized void init(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int carouselViewId, @NonNull CarouselAdapter carouselAdapter) {
        this.activity = activity;
        this.adapter = carouselAdapter;
        carouselView = view.findViewById(carouselViewId);
        carouselView.setLayoutManager(getManager());
        carouselView.setAdapter(adapter);
        carouselView.setAutoScroll(isAutoScroll());
    }

    /**
     * @param orientation
     * @param reverseLayout
     */
    public synchronized void setOrientation(@CarouselOrientation int orientation, boolean reverseLayout) {
        manager = new CarouselLayoutManager(activity, orientation, reverseLayout);
        carouselView.setLayoutManager(manager);
        int padding = 0;
        switch (orientation) {
            case CarouselView.HORIZONTAL:
                padding = ViewHelper.getScreenWidth() / 4;
                carouselView.setPadding(padding, 0, padding, 0);
                break;
            case CarouselView.VERTICAL:
                padding = ViewHelper.getScreenHeight() / 4;
                carouselView.setPadding(0, padding, 0, padding);
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
    private synchronized CarouselLayoutManager getManager() {
        if (manager == null) {
            setOrientation(CarouselView.HORIZONTAL, false);
        }
        return manager;
    }

    /**
     * @param items
     */
    @Deprecated
    public void addAll(@NonNull ArrayList items) {
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
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
    public void add(@NonNull CarouselModel items) {
        adapter.operation(items, CarouselAdapter.ADD);
        adapter.notifyDataSetChanged();
    }

    /**
     * @param items
     */
    public void remove(@NonNull CarouselModel items) {
        adapter.operation(items, CarouselAdapter.REMOVE);
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
        return carouselView.getCurrentPosition();
    }

    /**
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        carouselView.scrollToPosition(currentPosition);
    }

    /**
     * @param listener
     */
    public void setSnappingListener(CarouselListener listener) {
        carouselView.setListener(listener);
    }

    /**
     *
     */
    public void setAutoScrollPause() {
        carouselView.setAutoScrollPause();
    }


    /**
     *
     */
    public void setAutoScrollResume() {
        carouselView.setAutoScrollResume();
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
        carouselView.setAutoScroll(autoScroll);
        carouselView.setDelayMillis(delayMillis);
        carouselView.setLoopMode(loopMode);
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
