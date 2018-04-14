package ir.acharkit.android.component.progress;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/17/18
 * Email:   alirezat775@gmail.com
 */
public class LoadingIndicatorProgress extends AbstractProgress {

    private static final String TAG = LoadingIndicatorProgress.class.getName();

    public LoadingIndicatorProgress(Context context) {
        this(context, null);
    }

    public LoadingIndicatorProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingIndicatorProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void shapeView(ImageView imageView, int color, int colorStroke) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{8, 8, 8, 8});
        shape.setColor(color);
        shape.setStroke(2, colorStroke);
        imageView.setBackgroundDrawable(shape);
    }

    @Override
    public ValueAnimator animator() {
        return ValueAnimator.ofFloat(NO_SCALE, SCALE);
    }

    @Override
    public void setSelectedAnimate() {
        position = 0;
        ValueAnimator anim = animator();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ImageView view = imageViewList.get(position);
                view.setScaleX((float) animation.getAnimatedValue());
                view.setScaleY((float) animation.getAnimatedValue());
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                ImageView view = imageViewList.get(position);
                view.setScaleX(NO_SCALE);
                view.setScaleY(NO_SCALE);
                if (reverse) {
                    position++;
                    if (position == itemCount - 1)
                        reverse = false;
                } else {
                    position--;
                    if (position == 0)
                        reverse = true;
                }
            }
        });

        anim.setDuration(MIN_TIME_DURATION);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }
}
