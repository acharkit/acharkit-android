package ir.acharkit.android.component.progress;

import android.animation.Animator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/17/18
 * Email:   alirezat775@gmail.com
 */
public abstract class AbstractProgress extends LinearLayoutCompat {

    // duration const
    public static final int DEFAULT_TIME_DURATION = 500;
    public static final int MIN_TIME_DURATION = 200;
    public static final int MAX_TIME_DURATION = 1000;

    // translation const
    static final int MOVE_TO_LEFT = 180;
    static final int MOVE_TO_RIGHT = -180;
    static final int STAY_POSITION = 0;

    // scale view const
    static final int NO_SCALE = 1;
    static final float SCALE = 2f;
    static final float SCALE_X2 = 4f;

    // alpha view const
    static final int ALPHA_VISIBLE = 255;
    static final int ALPHA_UN_VISIBLE = 0;

    // rotate view const
    static final float ROTATE = 180;
    static final int FULL_ROTATE = 360;
    static final int NO_ROTATE = 0;

    static final int DEF_VALUE = 10;
    private static final String TAG = AbstractProgress.class.getName();
    final List<ImageView> imageViewList = new ArrayList<>();
    int itemSize = DEF_VALUE;
    int delimiterSize = DEF_VALUE;

    int itemCount;
    int position;
    boolean reverse = true;
    private int duration;
    private int color;

    public AbstractProgress(Context context) {
        this(context, null);
    }

    public AbstractProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public AbstractProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void changeLayout(int pageCount, int color, int duration) {
        removeAllViews();
        setColor(color);
        setDuration(duration);
        setItemCount(pageCount, 2);
    }

    public abstract Animator animator();

    public abstract void setSelectedAnimate();

    public abstract void shapeView(ImageView imageView, int color, int colorStroke);

    public void setItemCount(final int itemCount, int scaleSize) {
        this.itemCount = itemCount;
        removeAllViews();
        imageViewList.clear();
        for (int i = 0; i < itemCount; ++i) {
            addView(createBoxedItem(i, scaleSize));
        }
        setSelectedAnimate();
    }

    @NonNull
    public FrameLayout createBoxedItem(final int position, int scaleSize) {
        final FrameLayout box = new FrameLayout(getContext());
        final ImageView item = createItem(scaleSize);
        box.addView(item);
        imageViewList.add(item);
        final LinearLayoutCompat.LayoutParams boxParams = new LinearLayoutCompat.LayoutParams((itemSize * scaleSize), (itemSize * scaleSize));
        boxParams.width = boxParams.width * scaleSize;
        boxParams.height = boxParams.height * scaleSize;
        if (position >= 0) {
            int margin = delimiterSize / 2;
            boxParams.setMargins(margin, margin, margin, margin);
        }
        box.setLayoutParams(boxParams);
        return box;
    }

    @NonNull
    public ImageView createItem(int scaleSize) {
        final ImageView index = new ImageView(getContext());
        final FrameLayout.LayoutParams indexParams = new FrameLayout.LayoutParams(itemSize, itemSize);
        indexParams.gravity = Gravity.CENTER;
        indexParams.width = indexParams.width * scaleSize;
        indexParams.height = indexParams.height * scaleSize;
        index.setLayoutParams(indexParams);
        shapeView(index, getColor(), getColor());
        index.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return index;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
