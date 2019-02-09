package ir.acharkit.android.component.progress;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/17/18
 * Email:   alirezat775@gmail.com
 */
public class TranslationIndicatorProgress extends AbstractProgress {

    private int position;
    private boolean reverse = true;
    private boolean loop = true;

    public TranslationIndicatorProgress(Context context) {
        this(context, null);
    }

    public TranslationIndicatorProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslationIndicatorProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void shapeView(ImageView imageView, int color, int colorStroke) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{8, 8, 8, 8});
        shape.setColor(color);
        shape.setStroke(4, colorStroke);
        imageView.setBackgroundDrawable(shape);
    }

    private void setReverse() {
        if (reverse) {
            setRotationX(FULL_ROTATE);
            setRotationY(FULL_ROTATE);
        } else {
            setRotationX(ROTATE);
            setRotationY(ROTATE);
        }
    }

    @Override
    public void setItemCount(int itemCount, int scaleSize) {
        super.setItemCount(9, scaleSize);
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
        return ValueAnimator.ofFloat(STAY_POSITION, MOVE_TO_RIGHT);
    }

    @Override
    public void setSelectedAnimate() {
        position = 0;
        final ValueAnimator anim = animator();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ImageView view = imageViewList.get(position);
                if (reverse) {
                    view.setTranslationX(MOVE_TO_RIGHT);
                    setReverse();
                } else {
                    view.setTranslationX(STAY_POSITION);
                    setReverse();
                }
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
                    if (position == itemCount - 1) {
                        reverse = false;
                    }
                } else {
                    position--;
                    if (position == 0) {
                        reverse = true;
                    }
                }
            }
        });

        anim.setDuration(MIN_TIME_DURATION);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }
}
