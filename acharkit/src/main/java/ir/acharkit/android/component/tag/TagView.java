package ir.acharkit.android.component.tag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.util.helper.ConvertHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/25/18
 * Email:   alirezat775@gmail.com
 */
public class TagView extends ViewGroup {

    private final LayoutProcessor layoutProcessor;
    private int lineHeight;
    private int gravity = Gravity.LEFT;

    /**
     * @param context
     */
    public TagView(Context context) {
        this(context, null, 0);
    }

    /**
     * @param context
     * @param attrs
     */
    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutProcessor = new LayoutProcessor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);

        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int lineHeight = 0;

        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childW = child.getMeasuredWidth();
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + getVerticalSpacing());

                if (xPos + childW > width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                }

                xPos += childW + getMinimumHorizontalSpacing();
            }
        }
        this.lineHeight = lineHeight;

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED ||
                (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST && yPos + lineHeight < height)) {
            height = yPos + lineHeight;
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();
        layoutProcessor.setWidth(width);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childW = child.getMeasuredWidth();
                final int childH = child.getMeasuredHeight();
                if (xPos + childW > width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                    layoutProcessor.layoutPreviousRow();
                }
                layoutProcessor.addViewForLayout(child, yPos, childW, childH);
                xPos += childW + getMinimumHorizontalSpacing();
            }
        }
        layoutProcessor.layoutPreviousRow();
        layoutProcessor.clear();
    }

    private int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    private int getMinimumHorizontalSpacing() {
        return ConvertHelper.dpToPx(getContext(), 2);
    }

    private int getVerticalSpacing() {
        return ConvertHelper.dpToPx(getContext(), 2);
    }

    private class LayoutProcessor {

        private final List<View> viewsInCurrentRow;
        private final List<Integer> viewWidths;
        private final List<Integer> viewHeights;
        private int rowY;
        private int width;


        private LayoutProcessor() {
            viewsInCurrentRow = new ArrayList<>();
            viewWidths = new ArrayList<>();
            viewHeights = new ArrayList<>();
        }

        /**
         * @param view
         * @param yPos
         * @param childW
         * @param childH
         */
        void addViewForLayout(View view, int yPos, int childW, int childH) {
            rowY = yPos;
            viewsInCurrentRow.add(view);
            viewWidths.add(childW);
            viewHeights.add(childH);
        }

        /**
         *
         */
        void clear() {
            viewsInCurrentRow.clear();
            viewWidths.clear();
            viewHeights.clear();
        }

        /**
         *
         */
        void layoutPreviousRow() {
            int gravity = getGravity();
            int minimumHorizontalSpacing = getMinimumHorizontalSpacing();
            switch (gravity) {
                case Gravity.LEFT:
                    int xPos = getPaddingLeft();
                    for (int i = 0; i < viewsInCurrentRow.size(); i++) {
                        viewsInCurrentRow.get(i).layout(xPos, rowY, xPos + viewWidths.get(i), rowY + viewHeights.get(i));
                        xPos += viewWidths.get(i) + minimumHorizontalSpacing;
                    }
                    break;
                case Gravity.RIGHT:
                    int xEnd = width - getPaddingRight();
                    for (int i = viewsInCurrentRow.size() - 1; i >= 0; i--) {
                        int xStart = xEnd - viewWidths.get(i);
                        viewsInCurrentRow.get(i).layout(xStart, rowY, xEnd, rowY + viewHeights.get(i));
                        xEnd = xStart - minimumHorizontalSpacing;
                    }
                    break;
            }
            clear();
        }

        /**
         * @param width
         */
        void setWidth(int width) {
            this.width = width;
        }
    }
}
