package ir.acharkit.android.component.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.R;
import ir.acharkit.android.component.listener.OnPageChangeListener;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/25/17
 * Email:   alirezat775@gmail.com
 */
public class ViewPagerIndicator extends LinearLayoutCompat implements ViewPager.OnPageChangeListener {

    private static final float SCALE = 1.6f;
    private static final int NO_SCALE = 1;
    private static final int DEF_VALUE = 10;
    private static final int DEF_ICON = R.drawable.white_circle;
    private final List<ImageView> imageViewList = new ArrayList<>();
    private int itemSize = DEF_VALUE;
    private int delimiterSize = DEF_VALUE;
    private int itemIcon = DEF_VALUE;

    private int selected;
    private int pageCount;

    private ViewPager viewPager;
    private OnPageChangeListener onPageChangeListener;


    /**
     * @param context
     */
    public ViewPagerIndicator(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ViewPagerIndicator(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ViewPagerIndicator(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);
        try {
            itemSize = attributes.getDimensionPixelSize(R.styleable.ViewPagerIndicator_itemSize, DEF_VALUE);
            delimiterSize = attributes.getDimensionPixelSize(R.styleable.ViewPagerIndicator_delimiterSize, DEF_VALUE);
            itemIcon = attributes.getResourceId(R.styleable.ViewPagerIndicator_itemIcon, DEF_ICON);
        } finally {
            attributes.recycle();
        }
    }

    /**
     * @param viewPager
     */
    public void setupWithViewPager(@NonNull final ViewPager viewPager) {
        this.viewPager = viewPager;
        setPageCount(viewPager.getAdapter().getCount());
        viewPager.addOnPageChangeListener(this);
    }


    /**
     * @param selectedIndex
     */
    private void setSelectedIndex(final int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex > pageCount - 1)
            return;

        final ImageView unselectedView = imageViewList.get(selected);
        unselectedView.animate().scaleX(NO_SCALE).scaleY(NO_SCALE).setDuration(300).start();

        final ImageView selectedView = imageViewList.get(selectedIndex);
        selectedView.animate().scaleX(SCALE).scaleY(SCALE).setDuration(300).start();

        selected = selectedIndex;
    }

    /**
     * @param pageCount
     */
    private void setPageCount(final int pageCount) {
        this.pageCount = pageCount;
        selected = 0;
        removeAllViews();
        imageViewList.clear();

        for (int i = 0; i < pageCount; ++i) {
            addView(createBoxedItem(i));
        }
        setSelectedIndex(selected);
    }

    /**
     * @param position
     * @return
     */
    @NonNull
    private FrameLayout createBoxedItem(final int position) {
        final FrameLayout box = new FrameLayout(getContext());
        final ImageView item = createItem();
        box.addView(item);
        imageViewList.add(item);

        final LinearLayoutCompat.LayoutParams boxParams = new LinearLayoutCompat.LayoutParams((int) (itemSize * SCALE), (int) (itemSize * SCALE));
        if (position > 0) {
            boxParams.setMargins(delimiterSize, 0, 0, 0);
        }
        box.setLayoutParams(boxParams);
        return box;
    }

    /**
     * @param listener
     */
    public void addOnPageChangeListener(final OnPageChangeListener listener) {
        onPageChangeListener = listener;
    }

    /**
     * @return
     */
    @NonNull
    private ImageView createItem() {
        final ImageView index = new ImageView(getContext());
        final FrameLayout.LayoutParams indexParams = new FrameLayout.LayoutParams(itemSize, itemSize);
        indexParams.gravity = Gravity.CENTER;
        index.setLayoutParams(indexParams);
        index.setImageResource(itemIcon);
        index.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return index;
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        if (onPageChangeListener != null)
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);

    }

    @Override
    public void onPageSelected(final int position) {
        setSelectedIndex(position);
        if (onPageChangeListener != null)
            onPageChangeListener.onPageSelected(position);

    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        if (onPageChangeListener != null)
            onPageChangeListener.onPageScrollStateChanged(state);

    }
}
