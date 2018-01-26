package ir.acharkit.android.component.cardList;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public class CardListView extends RecyclerView {

    //carousel orientation
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    //anchor default
    private static final int CENTER = 0;

    private final static String TAG = CardListView.class.getName();
    private CardListSnappingListener listener;
    private VelocityTracker velocityTracker = null;
    private int anchor;
    private float scrollSpeed;
    private int currentPosition;
    private boolean actionDown = true;
    private boolean autoScroll = false;
    private boolean loopMode;
    private long delayMillis = 5000;
    private boolean reverseLoop = true;
    private Scheduler scheduler;

    /**
     * @param context
     */
    public CardListView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CardListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CardListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param layout
     */
    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        init();
    }

    /**
     *
     */
    private void init() {
        setClipToPadding(false);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setAnchor(CENTER);
        addOnItemTouchListener(onItemTouchListener());
        post(new Runnable() {
            @Override
            public void run() {
                scrolling(0);
                if (isAutoScroll()) {
                    getScheduler();
                }
            }
        });
    }

    /**
     * @return
     */
    public Scheduler getScheduler() {
        if (scheduler == null) {
            scheduler = new Scheduler(getDelayMillis(), 1);
        }
        return scheduler;
    }

    /**
     *
     */
    public void setAutoScrollPause() {
        if (getScheduler() != null)
            getScheduler().cancel();
    }


    /**
     *
     */
    public void setAutoScrollResume() {
        if (getScheduler() != null)
            getScheduler().start();
    }


    /**
     * @return
     */
    private boolean isRTL() {
        return ViewCompat.getLayoutDirection(CardListView.this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * @return
     */
    private OnItemTouchListener onItemTouchListener() {
        return new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (actionDown) {
                            actionDown = false;
                            if (velocityTracker == null) {
                                velocityTracker = VelocityTracker.obtain();
                            } else {
                                velocityTracker.clear();
                            }
                            velocityTracker.addMovement(e);
                        } else {
                            velocityTracker.addMovement(e);
                            velocityTracker.computeCurrentVelocity(1000);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        actionDown = true;
                        if (velocityTracker != null) {
                            switch (getManager().getOrientation()) {
                                case HORIZONTAL:
                                    if (velocityTracker.getXVelocity() <= 0) {
                                        if (!isTrustLayout())
                                            scrolling(-1);// rtl or reverse mode
                                        else scrolling(1);//scroll to right
                                    } else if (velocityTracker.getXVelocity() > 0) {
                                        if (!isTrustLayout())
                                            scrolling(1);// rtl or reverse mode
                                        else scrolling(-1);//scroll to left
                                    }
                                    break;
                                case VERTICAL:
                                    if (velocityTracker.getYVelocity() <= 0) {
                                        if (getManager().getReverseLayout())
                                            scrolling(-1);// rtl or reverse mode
                                        else scrolling(1);//scroll to up
                                    } else if (velocityTracker.getYVelocity() > 0) {
                                        if (getManager().getReverseLayout())
                                            scrolling(1);// rtl or reverse mode
                                        else scrolling(-1);//scroll to down
                                    }
                                    break;
                            }
                            velocityTracker.recycle();
                            velocityTracker = null;
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
    }

    /**
     * @return
     */
    private boolean isTrustLayout() {
        if (isRTL() && getManager().getReverseLayout()) {
            return true;
        } else if (!isRTL() && getManager().getReverseLayout()) {
            return false;
        } else if (isRTL() && !getManager().getReverseLayout()) {
            return false;
        } else if (!isRTL() && !getManager().getReverseLayout()) {
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public CardListSnappingListener getListener() {
        return listener != null ? listener : new CardListSnappingListener() {
            @Override
            public void onPositionChange(int position) {

            }

            @Override
            public void onScroll(int dx, int dy) {

            }
        };
    }

    /**
     * @param listener
     */
    public void setListener(CardListSnappingListener listener) {
        this.listener = listener;
    }

    /**
     * @return
     */
    public int getAnchor() {
        return anchor;
    }

    /**
     * @param anchor
     */
    public void setAnchor(int anchor) {
        if (this.anchor != anchor) {
            this.anchor = anchor;
            getManager().setAnchor(anchor);
            requestLayout();
        }
    }

    /**
     * @param dx
     * @param dy
     */
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (getListener() != null)
            getListener().onScroll(dx, dy);

    }

    /**
     * @param position
     */
    @Override
    public void scrollToPosition(final int position) {
        super.scrollToPosition(position);
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollToPosition(position);
            }
        });
    }

    /**
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            scrolling(0);
            if (isAutoScroll()) {
                if (getScheduler() != null)
                    getScheduler().start();
            }

            if (getCurrentPosition() == 0)
                reverseLoop = true;
            else if (getCurrentPosition() == getAdapter().getItemCount() - 1)
                reverseLoop = false;
        }
    }

    /**
     * @param positionPlus
     */
    private void scrolling(int positionPlus) {
        int centerViewPosition = calculateSnapViewPosition() + (positionPlus);
        if (centerViewPosition <= 0)
            centerViewPosition = 0;
        else if (centerViewPosition >= getAdapter().getItemCount() - 1)
            centerViewPosition = getAdapter().getItemCount() - 1;

        smoothScrollToPosition(centerViewPosition);

        if (getListener() != null)
            getListener().onPositionChange(centerViewPosition);

        setCurrentPosition(centerViewPosition);
    }

    /**
     * @return
     */
    private int getParentAnchor() {
        return (getManager().getOrientation() == VERTICAL ? getHeight() : getWidth()) / 2;
    }

    /**
     * @param view
     * @return
     */
    private int getViewAnchor(View view) {
        return (getManager().getOrientation() == VERTICAL ? view.getTop() + (view.getHeight() / 2) : view.getLeft() + (view.getWidth() / 2));
    }

    /**
     * @return
     */
    private int calculateSnapViewPosition() {
        int parentAnchor = getParentAnchor();
        int lastVisibleItemPosition = getManager().findLastVisibleItemPosition();
        int firstVisibleItemPosition = getManager().findFirstVisibleItemPosition();

        View currentViewClosestToAnchor = getManager().findViewByPosition(firstVisibleItemPosition);
        int currentViewClosestToAnchorDistance = parentAnchor - getViewAnchor(currentViewClosestToAnchor);
        int currentViewClosestToAnchorPosition = firstVisibleItemPosition;

        for (int i = firstVisibleItemPosition + 1; i <= lastVisibleItemPosition; i++) {
            View view = getManager().findViewByPosition(i);
            int distanceToCenter = parentAnchor - getViewAnchor(view);
            if (Math.abs(distanceToCenter) < Math.abs(currentViewClosestToAnchorDistance)) {
                currentViewClosestToAnchorPosition = i;
                currentViewClosestToAnchorDistance = distanceToCenter;
            }
        }
        return currentViewClosestToAnchorPosition;
    }

    /**
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }


    /**
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * @return
     */
    public CardListLayoutManager getManager() {
        return (CardListLayoutManager) getLayoutManager();
    }

    /**
     * @return
     */
    public boolean isAutoScroll() {
        return autoScroll;
    }

    /**
     * @param autoScroll
     */
    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    /**
     * @return
     */
    public long getDelayMillis() {
        return delayMillis;
    }

    /**
     * @param delayMillis
     */
    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * @return
     */
    public boolean isLoopMode() {
        return loopMode;
    }

    /**
     * @param loopMode
     */
    public void setLoopMode(boolean loopMode) {
        this.loopMode = loopMode;
    }

    public class Scheduler extends CountDownTimer {

        public Scheduler(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (isLoopMode()) {
                if (reverseLoop)
                    scrolling(+1);
                else
                    scrolling(-1);

            } else {
                if (getCurrentPosition() >= getAdapter().getItemCount() - 1)
                    scrollToPosition(0);
            }
            cancel();
            start();
        }
    }
}
