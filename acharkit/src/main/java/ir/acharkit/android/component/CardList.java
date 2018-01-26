package ir.acharkit.android.component;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ir.acharkit.android.annotation.CardListOrientation;
import ir.acharkit.android.component.cardList.CardListLayoutManager;
import ir.acharkit.android.component.cardList.CardListSnappingListener;
import ir.acharkit.android.component.cardList.CardListView;
import ir.acharkit.android.component.cardList.adapter.CardListAdapter;
import ir.acharkit.android.component.cardList.model.CardListModel;
import ir.acharkit.android.util.helper.ViewHelper;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class CardList {

    private static final String TAG = CardList.class.getName();
    private CardListView cardListView;
    private CardListAdapter adapter;
    private AppCompatActivity activity;
    private CardListLayoutManager manager;
    private int currentPosition;
    private boolean autoScroll = false;

    /**
     * @param activity
     * @param carouselViewId
     * @param cardListAdapter
     */
    public CardList(@NonNull AppCompatActivity activity, @IdRes int carouselViewId, @NonNull CardListAdapter cardListAdapter) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, carouselViewId, cardListAdapter);
    }

    /**
     * @param activity
     * @param view
     * @param carouselViewId
     * @param cardListAdapter
     */
    public CardList(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int carouselViewId, @NonNull CardListAdapter cardListAdapter) {
        init(activity, view, carouselViewId, cardListAdapter);
    }

    /**
     * @param activity
     * @param view
     * @param carouselViewId
     * @param cardListAdapter
     */
    private void init(@NonNull AppCompatActivity activity, @NonNull View view, @IdRes int carouselViewId, @NonNull CardListAdapter cardListAdapter) {
        this.activity = activity;
        this.adapter = cardListAdapter;
        cardListView = view.findViewById(carouselViewId);
        cardListView.setLayoutManager(getManager());
        cardListView.setAdapter(adapter);
        cardListView.setAutoScroll(isAutoScroll());
    }

    /**
     * @param orientation
     * @param reverseLayout
     */
    public void setOrientation(@CardListOrientation int orientation, boolean reverseLayout) {
        manager = new CardListLayoutManager(activity, orientation, reverseLayout);
        cardListView.setLayoutManager(manager);
        int padding = 0;
        switch (orientation) {
            case CardListView.HORIZONTAL:
                padding = ViewHelper.getScreenWidth() / 4;
                cardListView.setPadding(padding, 0, padding, 0);
                break;
            case CardListView.VERTICAL:
                padding = ViewHelper.getScreenHeight() / 4;
                cardListView.setPadding(0, padding, 0, padding);
                break;
        }
    }

    public void setScaleView(boolean scaleView) {
        getManager().setScaleView(scaleView);
    }

    /**
     * @return
     */
    private CardListLayoutManager getManager() {
        if (manager != null)
            return manager;
        else
            setOrientation(CardListView.HORIZONTAL, false);
        return manager;
    }

    /**
     * @param items
     */
    public void add(@NonNull CardListModel items) {
        adapter.operation(items, CardListAdapter.ADD);
        adapter.notifyDataSetChanged();
    }

    /**
     * @param items
     */
    public void remove(@NonNull CardListModel items) {
        adapter.operation(items, CardListAdapter.REMOVE);
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
        return cardListView.getCurrentPosition();
    }

    /**
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        cardListView.setCurrentPosition(currentPosition);
        cardListView.smoothScrollToPosition(currentPosition);
    }

    /**
     * @param listener
     */
    public void setSnappingListener(CardListSnappingListener listener) {
        cardListView.setListener(listener);
    }

    /**
     *
     */
    public void setAutoScrollPause() {
        cardListView.setAutoScrollPause();
    }


    /**
     *
     */
    public void setAutoScrollResume() {
        cardListView.setAutoScrollResume();
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
        cardListView.setAutoScroll(autoScroll);
        cardListView.setDelayMillis(delayMillis);
        cardListView.setLoopMode(loopMode);
    }
}
