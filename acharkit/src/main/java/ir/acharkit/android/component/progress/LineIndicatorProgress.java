package ir.acharkit.android.component.progress;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/17/18
 * Email:   alirezat775@gmail.com
 */
public class LineIndicatorProgress extends AbstractProgress {

    private int position;
    private boolean reverse = true;

    public LineIndicatorProgress(Context context) {
        super(context, null);
    }

    public LineIndicatorProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LineIndicatorProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void shapeView(ImageView imageView, int color, int colorStroke) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.LINE);
        shape.setCornerRadii(new float[]{8, 8, 8, 8, 0, 0, 0, 0});
        shape.setColor(color);
        shape.setStroke(4, colorStroke);
        imageView.setBackgroundDrawable(shape);
    }

    @Override
    public void setItemCount(int itemCount, int scaleSize) {
        super.setItemCount(5, scaleSize);
    }

    @NonNull
    @Override
    public ImageView createItem(int scaleSize) {
        final ImageView index = new ImageView(getContext());
        final FrameLayout.LayoutParams indexParams = new FrameLayout.LayoutParams(itemSize, itemSize);
        indexParams.gravity = Gravity.CENTER;
        indexParams.width = indexParams.width * scaleSize;
        indexParams.height = indexParams.height * scaleSize;
        index.setLayoutParams(indexParams);
        shapeView(index, getColor(), getColor());
        index.setScaleType(ImageView.ScaleType.FIT_CENTER);
        index.setScaleX(SCALE);
        index.setScaleY(SCALE);
        return index;
    }

    @Override
    public ValueAnimator animator() {
        return ValueAnimator.ofFloat(NO_ROTATE, ROTATE);
    }

    @Override
    public void setSelectedAnimate() {
        position = 0;
        ValueAnimator anim = animator();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ImageView view = imageViewList.get(position);
                view.setRotationX((float) animation.getAnimatedValue());
                view.setRotationY((float) animation.getAnimatedValue());
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

        anim.setDuration(DEFAULT_TIME_DURATION);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }
}
