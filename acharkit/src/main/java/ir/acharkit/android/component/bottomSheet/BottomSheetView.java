package ir.acharkit.android.component.bottomSheet;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import ir.acharkit.android.component.BottomSheet;
import ir.acharkit.android.util.Colour;
import ir.acharkit.android.util.Logger;
import ir.acharkit.android.util.helper.SwipeHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    5/17/18
 * Email:   alirezat775@gmail.com
 */
public class BottomSheetView extends LinearLayoutCompat {

    public static final int DEFAULT_COLOR = Colour.GRAY;
    private static final String TAG = BottomSheetView.class.getName();
    private final int durationMilliseconds = 300;
    private int bottomSheetColor;
    private Context context;
    private int widthBottomSheet;
    private int heightBottomSheet;
    private boolean isCollapsed = false;
    private BottomSheet.Callback callback;

    public BottomSheetView(Context context) {
        this(context, null);
    }

    public BottomSheetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSheetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     *
     */
    @SuppressLint("WrongConstant")
    private void init() {
        setSize(1f, 0.5f);
        setBackgroundColor(getBottomSheetColor());
        setOrientation(LinearLayout.VERTICAL);
        setOnTouchListener(new SwipeHelper(context) {
            @Override
            public void onSwipeRight() {
                Logger.d(TAG, "onSwipeRight");
            }

            @Override
            public void onSwipeLeft() {
                Logger.d(TAG, "onSwipeLeft");
            }

            @Override
            public void onSwipeTop() {
                Logger.d(TAG, "onSwipeTop");
                if (!isCollapsed) {
                } else {
                    show();
                }
            }

            @Override
            public void onSwipeBottom() {
                Logger.d(TAG, "onSwipeBottom");
                collapse();
            }
        });
    }

    /**
     * @param width  set width of bottomSheet (number must be less than 1 and greater than 0)
     * @param height set height of bottomSheet (number must be less than 1 and greater than 0)
     * @return
     */
    public void setSize(float width, float height) {
        if (width > 1 || height > 1 || width <= 0 || height <= 0) {
            throw new NumberFormatException("The number entered must be less than 1 and greater than 0");
        }
        widthBottomSheet = (int) (context.getResources().getDisplayMetrics().widthPixels * width);
        heightBottomSheet = (int) (context.getResources().getDisplayMetrics().heightPixels * height);

        post(new Runnable() {
            @Override
            public void run() {
                getLayoutParams().width = widthBottomSheet;
                getLayoutParams().height = heightBottomSheet;
                requestLayout();
            }
        });
    }

    /**
     *
     */
    public void collapse() {
        if (isCollapsed)
            return;
        post(new Runnable() {
            @Override
            public void run() {
                getCallback().collapse();
                isCollapsed = true;
                TopToBottomAnimation animation = new TopToBottomAnimation();
                startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        getLayoutParams().width = widthBottomSheet;
                        getLayoutParams().height = 0;
                        requestLayout();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });
    }

    /**
     *
     */
    public void show() {
        if (!isCollapsed)
            return;
        post(new Runnable() {
            @Override
            public void run() {
                getCallback().show();
                isCollapsed = false;
                BottomToTopAnimation animation = new BottomToTopAnimation();
                startAnimation(animation);
                getLayoutParams().width = widthBottomSheet;
                getLayoutParams().height = heightBottomSheet;
                requestLayout();
            }
        });
    }

    /**
     * @return
     */
    private int getBottomSheetColor() {
        return (bottomSheetColor == 0) ? DEFAULT_COLOR : bottomSheetColor;
    }

    /**
     * @param bottomSheetColor
     */
    public void setBottomSheetColor(int bottomSheetColor) {
        this.bottomSheetColor = bottomSheetColor;
        setBackgroundColor(bottomSheetColor);
    }

    /**
     * @return
     */
    private BottomSheet.Callback getCallback() {
        return callback;
    }

    /**
     * @param callback
     */
    public void setCallback(BottomSheet.Callback callback) {
        this.callback = callback;
    }

    /**
     *
     */
    private class TopToBottomAnimation extends TranslateAnimation {
        public TopToBottomAnimation() {
            super(Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, heightBottomSheet);

            setDuration(durationMilliseconds);
            setFillAfter(false);
        }
    }

    /**
     *
     */
    private class BottomToTopAnimation extends TranslateAnimation {
        public BottomToTopAnimation() {
            super(Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, heightBottomSheet,
                    Animation.ABSOLUTE, 0);

            setDuration(durationMilliseconds);
            setFillAfter(false);
        }
    }
}
