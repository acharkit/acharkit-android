package ir.acharkit.android.component.progress;

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
public class FadeProgress extends AbstractProgress {

    private int position;

    public FadeProgress(Context context) {
        this(context, null);
    }

    public FadeProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FadeProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    @Override
    public void setItemCount(int itemCount, int scaleSize) {
        super.setItemCount(1, 6);
    }

    @Override
    public ValueAnimator animator() {
        return ValueAnimator.ofFloat(ALPHA_UN_VISIBLE, ALPHA_VISIBLE);
    }

    @Override
    public void setSelectedAnimate() {
        final float ratio = ALPHA_VISIBLE / SCALE_X2;
        position = 0;
        ValueAnimator anim = animator();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ImageView view = imageViewList.get(position);
                view.getBackground().setAlpha(-Math.round((Float) animation.getAnimatedValue()));
                view.setScaleX((float) animation.getAnimatedValue() / ratio);
                view.setScaleY((float) animation.getAnimatedValue() / ratio);
            }
        });

        anim.setDuration(MAX_TIME_DURATION);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }
}
