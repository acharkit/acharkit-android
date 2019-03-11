package ir.acharkit.android.component.carousel;

import android.content.Context;
import android.graphics.PointF;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public class CarouselLayoutManager extends LinearLayoutManager {

    private static final String TAG = CarouselLayoutManager.class.getName();
    private final float shrinkAmount = 0.15f;
    private final float shrinkDistance = 0.9f;
    private SmoothScroller smoothScroller;
    private int anchor;
    private boolean scaleView = true;

    /**
     * @param context Current context, will be used to access resources
     */
    public CarouselLayoutManager(Context context) {
        super(context);
    }

    /**
     * @param context current context, will be used to access resources
     * @param orientation how to arrange items vertical or horizontal
     */
    public CarouselLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
    }

    /**
     * @param context current context, will be used to access resources
     * @param orientation how to arrange items vertical or horizontal
     * @param reverseLayout reverse the list display
     */
    public CarouselLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        smoothScroller = new SmoothScroller(context);
    }

    /**
     * @param context current context, will be used to access resources
     * @param attrs attributeSet
     * @param defStyleAttr defStyleAttr
     * @param defStyleRes defStyleRes
     */
    public CarouselLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        smoothScroller = new SmoothScroller(context);
    }


    /**
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollVerticallyBy(0, recycler, state);
        scrollHorizontallyBy(0, recycler, state);
    }

    /**
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            if (isScaleView()) {
                float midpoint = getHeight() / 2.f;
                float d0 = 0.f;
                float d1 = shrinkDistance * midpoint;
                float s0 = 1.f;
                float s1 = 1.f - shrinkAmount;
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    float childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
                    float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                    float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    /**
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            if (isScaleView()) {
                float midpoint = getWidth() / 2.f;
                float d0 = 0.f;
                float d1 = shrinkDistance * midpoint;
                float s0 = 1.f;
                float s1 = 1.f - shrinkAmount;
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                    float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                    float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    /**
     * @return center view scale
     */

    public boolean isScaleView() {
        return scaleView;
    }

    /**
     * @param scaleView set center view scale
     */
    public void setScaleView(boolean scaleView) {
        this.scaleView = scaleView;
    }

    /**
     * @return
     */
    public int getAnchor() {
        return anchor;
    }

    /**
     * @param anchor set snap position
     */
    public void setAnchor(int anchor) {
        this.anchor = anchor;
    }

    /**
     * @param scrollSpeed milliSecond per inch
     */
    public void setScrollSpeed(float scrollSpeed) {
        smoothScroller.setScrollSpeed(scrollSpeed);
    }

    /**
     * @param recyclerView
     * @param state
     * @param position
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }


    public class SmoothScroller extends LinearSmoothScroller {

        //scrolling speed
        private static final float MILLISECONDS_PER_INCH = 200f;

        private float milliSecondsPerInch = -1;

        /**
         * @param context
         */
        public SmoothScroller(Context context) {
            super(context);
        }

        /**
         * @param scrollSpeed milliSecond per inch
         */
        public void setScrollSpeed(float scrollSpeed) {
            this.milliSecondsPerInch = scrollSpeed;
        }

        /**
         * @param targetPosition
         * @return
         */
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return CarouselLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

        /**
         * @param viewStart
         * @param viewEnd
         * @param boxStart
         * @param boxEnd
         * @param snapPreference
         * @return position view
         */
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return ((boxStart + boxEnd) / 2) - ((viewStart + viewEnd) / 2);
        }

        /**
         * @param displayMetrics
         * @return
         */
        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return (milliSecondsPerInch > 0 ? milliSecondsPerInch : MILLISECONDS_PER_INCH) / displayMetrics.densityDpi;
        }
    }
}