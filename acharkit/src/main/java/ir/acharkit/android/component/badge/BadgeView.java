package ir.acharkit.android.component.badge;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import ir.acharkit.android.annotation.BadgeViewPosition;
import ir.acharkit.android.util.Colour;
import ir.acharkit.android.util.Font;
import ir.acharkit.android.util.Log;
import ir.acharkit.android.util.helper.ConvertHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/23/18
 * Email:   alirezat775@gmail.com
 */
public class BadgeView extends AppCompatTextView {

    public static final int POSITION_TOP_LEFT = 1;
    public static final int POSITION_TOP_RIGHT = 2;
    public static final int POSITION_BOTTOM_LEFT = 3;
    public static final int POSITION_BOTTOM_RIGHT = 4;
    public static final int POSITION_CENTER = 5;
    public static final int POSITION_CENTER_TOP = 6;
    public static final int POSITION_CENTER_BOTTOM = 7;

    private static final int DEFAULT_MARGIN_DP = 5;
    private static final int DEFAULT_PADDING_DP = 12;
    private static final int DEFAULT_CORNER_RADIUS_DP = 42;
    private static final int DEFAULT_BADGE_COLOR = Colour.RED;
    private static final int DEFAULT_TEXT_COLOR = Colour.WHITE;

    private static final int DEFAULT_TEXT_SIZE = 9;

    private static final float FULL_ALPHA = 1f;
    private static final float NO_ALPHA = 0f;

    private static final int ANIMATION_DURATION = 300;
    private static final float SCALE = 1f;
    private static final String TAG = BadgeView.class.getName();
    private static final int WIDTH = FrameLayout.LayoutParams.WRAP_CONTENT;
    private static final int HEIGHT = 14;
    private static final String OVER_NUMBER = "999+";

    private View parentView;

    private int number;
    private int typefaceText;
    private String pathFont;

    private int badgeColor;
    private int textColor;

    private int badgePosition;
    private int badgeMarginH = DEFAULT_MARGIN_DP;
    private int badgeMarginV = DEFAULT_MARGIN_DP;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    /**
     * validate text changing
     */
    private void initText() {
        setVisibility(VISIBLE);
        setGravity(Gravity.CENTER);
        shapeView();
        initLayoutParams();
        if (getPathFont() != null)
            Font.fromAsset(getContext(), getPathFont(), getTypefaceText(), this);

        if (getNumber() < 999)
            setText(String.valueOf(getNumber()));
        else
            setText(OVER_NUMBER);

        setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
        setTextColor(getTextColor());

        if (getParentView() != null)
            initContainer(getParentView());
    }

    /**
     *
     */
    private void initLayoutParams() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ConvertHelper.dpToPx(getContext(), WIDTH), ConvertHelper.dpToPx(getContext(), HEIGHT));
        switch (getBadgePosition()) {
            case POSITION_TOP_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.TOP;
                lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
                break;
            case POSITION_TOP_RIGHT:
            default:
                lp.gravity = Gravity.RIGHT | Gravity.TOP;
                lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
                break;
            case POSITION_BOTTOM_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
                lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
                break;
            case POSITION_BOTTOM_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
                break;
            case POSITION_CENTER:
                lp.gravity = Gravity.CENTER;
                lp.setMargins(0, 0, 0, 0);
                break;
            case POSITION_CENTER_TOP:
                lp.gravity = Gravity.CENTER | Gravity.TOP;
                lp.setMargins(0, badgeMarginV, 0, 0);
                break;
            case POSITION_CENTER_BOTTOM:
                lp.gravity = Gravity.CENTER | Gravity.BOTTOM;
                lp.setMargins(0, 0, 0, badgeMarginV);
                break;
        }
        setLayoutParams(lp);
    }

    /**
     * create shapeView for background
     */
    private void shapeView() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(DEFAULT_CORNER_RADIUS_DP);
        shape.setColor(getBadgeColor());
        setBackgroundDrawable(shape);
        setPadding(DEFAULT_PADDING_DP, 0, DEFAULT_PADDING_DP, 0);
    }

    /**
     * @param pathFont
     * @param typeface
     */
    public void setFont(String pathFont, int typeface) {
        this.typefaceText = typeface;
        this.pathFont = pathFont;
    }

    /**
     * @return
     */
    private String getPathFont() {
        return pathFont;
    }

    /**
     * @return
     */
    private int getTypefaceText() {
        return typefaceText;
    }

    /**
     * @param view
     */
    private void initContainer(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        ViewParent parent = view.getParent();
        FrameLayout container = new FrameLayout(getContext());

        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(view);

        group.removeView(view);
        group.addView(container, index, lp);

        container.addView(view);
        container.addView(this);

        group.invalidate();

    }

    /**
     * @param badgeColor
     * @param textColor
     */
    public void setBadgeColor(@ColorInt int badgeColor, @ColorInt int textColor) {
        this.badgeColor = badgeColor;
        this.textColor = textColor;
    }

    /**
     * @return
     */
    private int getBadgeColor() {
        if (badgeColor == 0)
            badgeColor = DEFAULT_BADGE_COLOR;
        return badgeColor;
    }

    /**
     * @return
     */
    private int getTextColor() {
        if (textColor == 0)
            textColor = DEFAULT_TEXT_COLOR;
        return textColor;
    }

    /**
     * @return
     */
    private View getParentView() {
        return parentView;
    }

    /**
     * @param parentView
     */
    public void setParentView(@NonNull View parentView) {
        this.parentView = parentView;
    }

    /**
     * @return
     */
    private int getNumber() {
        return number;
    }

    /**
     * @param number
     */
    public void setNumber(int number) {
        if (number < 1)
            Log.e(TAG, "Number must be bigger than zero", null);
        this.number = number;
    }

    /**
     * @return
     */
    private int getBadgePosition() {
        return badgePosition;
    }

    /**
     * @param badgePosition
     */
    public void setBadgePosition(@BadgeViewPosition int badgePosition) {
        this.badgePosition = badgePosition;
    }

    /**
     *
     */
    public void show() {
        Animation fadeIn = new AlphaAnimation(NO_ALPHA, FULL_ALPHA);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(ANIMATION_DURATION);

        ScaleAnimation zoomIn = new ScaleAnimation(0, SCALE, 0, SCALE,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomIn.setDuration(ANIMATION_DURATION);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        animation.addAnimation(zoomIn);
        setAnimation(animation);

        initText();
        invalidate();
        requestLayout();
    }

    /**
     *
     */
    public void hide() {
        if (getVisibility() == GONE)
            return;

        Animation fadeOut = new AlphaAnimation(FULL_ALPHA, NO_ALPHA);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(ANIMATION_DURATION);

        ScaleAnimation zoomOut = new ScaleAnimation(SCALE, 0, SCALE, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomOut.setDuration(ANIMATION_DURATION);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeOut);
        animation.addAnimation(zoomOut);
        setAnimation(animation);

        setVisibility(GONE);
    }
}
