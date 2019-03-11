package ir.acharkit.android.component.bottomTab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ir.acharkit.android.component.BottomTab;
import ir.acharkit.android.component.badge.BadgeView;
import ir.acharkit.android.util.Colour;
import ir.acharkit.android.util.Font;
import ir.acharkit.android.util.helper.ConvertHelper;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/10/18
 * Email:   alirezat775@gmail.com
 */
public class BottomTabView extends LinearLayout {

    private static final String TAG = BottomTabView.class.getName();
    private static final int DEFAULT_COLOR = Colour.LTGRAY;
    private static final int DEFAULT_PADDING = 16;
    private static final int TAB_HEIGHT = 56;

    // text animation scale icon
    private static final float SCALE = 1.3f;
    private static final float NO_SCALE = 1.3f;
    private static final float FULL_ALPHA = 1.0f;
    private static final float SEMI_ALPHA = 0.5f;

    private HashMap<LinearLayoutCompat, Integer> itemLayoutList = new HashMap<>();
    private ArrayList<TextView> textViewList = new ArrayList<>();
    private ArrayList<ImageView> imageViewList = new ArrayList<>();
    private HashMap<Integer, BadgeView> badgeList = new HashMap<>();
    private int typeface;
    private String pathFont;
    private int selected = 0;
    private BottomTab.EnableType enableType;
    private TabChangeListener tabChangeListener;
    private int backgroundColor = DEFAULT_COLOR;
    private int activeColor;
    private int inActiveColor;

    /**
     * @param context
     */
    public BottomTabView(Context context) {
        super(context, null);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public BottomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public BottomTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                getLayoutParams().height = ConvertHelper.dpToPx(getContext(), TAB_HEIGHT);
                setOrientation(HORIZONTAL);
                setBackgroundColor(backgroundColor);
                setSelected(0);
                requestLayout();
            }
        });
    }

    /**
     * @param pathFont
     * @param typeface
     */
    public void setFont(String pathFont, int typeface) {
        this.typeface = typeface;
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
    private int getTypeface() {
        return typeface;
    }

    /**
     * @return
     */
    @SuppressLint("WrongConstant")
    private LinearLayoutCompat item() {
        final LinearLayoutCompat layoutCompat = new LinearLayoutCompat(getContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        param.gravity = Gravity.BOTTOM | Gravity.CENTER;
        layoutCompat.setLayoutParams(param);
        layoutCompat.setOrientation(LinearLayout.VERTICAL);

        // selector effect item
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        layoutCompat.setBackgroundResource(outValue.resourceId);

        return layoutCompat;
    }

    /**
     * type of animation
     * when selected tab choose one of enum type animation
     */
    private void animation() {
        switch (getEnableType()) {
            case TRANSLATION_ANIMATION:
                translationAnimation();
                break;
            case ALPHA_ANIMATION:
                alphaAnimation();
                break;
            case NO_ANIMATION:
            default:
                noAnimation();
                break;
        }
    }

    /**
     * created colorful animation
     */
    private void noAnimation() {
        for (int i = 0; i < itemLayoutList.size(); i++) {
            if (i == getSelected()) {
                final TextView textView = textViewList.get(i);
                final ImageView imageView = imageViewList.get(i);
                imageView.setImageDrawable(Colour.changeColorDrawable(getContext(), imageViewList.get(i), getActiveColor()));
                if (textView != null) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setTextColor(getActiveColor());
                        }
                    });
                }
            } else {
                final TextView textView = textViewList.get(i);
                final ImageView imageView = imageViewList.get(i);
                imageView.setImageDrawable(Colour.changeColorDrawable(getContext(), imageViewList.get(i), getInActiveColor()));
                if (textView != null) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setTextColor(getInActiveColor());
                        }
                    });
                }
            }
        }
    }

    /**
     * translation animation text and imageView
     */
    private void translationAnimation() {
        for (int i = 0; i < textViewList.size(); i++) {
            if (i == getSelected()) {
                final TextView textView = textViewList.get(i);
                final ImageView imageView = imageViewList.get(i);

                imageView.animate().translationY(0).scaleX(SCALE).scaleY(SCALE);
                imageView.setImageDrawable(Colour.changeColorDrawable(getContext(), imageViewList.get(i), getActiveColor()));

                if (textView != null) {
                    textView.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            textView.setVisibility(VISIBLE);
                            textView.setTextColor(getActiveColor());
                        }
                    });
                }
            } else {
                final TextView textView = textViewList.get(i);
                final ImageView imageView = imageViewList.get(i);

                imageView.animate().translationY(20).scaleX(NO_SCALE).scaleY(NO_SCALE);
                imageView.setImageDrawable(Colour.changeColorDrawable(getContext(), imageViewList.get(i), getInActiveColor()));

                if (textView != null) {
                    textView.animate().translationY(textView.getHeight()).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            textView.setVisibility(INVISIBLE);
                            textView.setTextColor(getInActiveColor());
                        }
                    });
                }
            }
        }
    }

    /**
     * reduce alpha non-selected item
     */
    private void alphaAnimation() {
        final int alphaInActiveColor = Colour.adjustAlpha(getInActiveColor(), SEMI_ALPHA);
        for (int i = 0; i < itemLayoutList.size(); i++) {
            if (i == getSelected()) {
                final TextView textView = textViewList.get(i);
                final ImageView imageView = imageViewList.get(i);
                if (textView != null) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setTextColor(getActiveColor());
                        }
                    });

                }
                imageView.setImageDrawable(Colour.changeColorDrawable(getContext(), imageView, getActiveColor()));
            } else {
                final TextView textView = textViewList.get(i);
                final ImageView imageView = imageViewList.get(i);
                if (textView != null) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setTextColor(alphaInActiveColor);
                        }
                    });
                }
                imageView.setImageDrawable(Colour.changeColorDrawable(getContext(), imageView, alphaInActiveColor));
            }
        }
    }

    /**
     * @param title
     * @param icon
     */
    public void addItem(String title, int icon) {
        final LinearLayoutCompat layoutCompat = item();
        if (icon != 0) {
            ImageView imageView = addImage(icon);
            layoutCompat.addView(imageView);
            imageViewList.add(imageView);
        }
        if (title != null && !title.isEmpty()) {
            TextView textView = addText(title);
            layoutCompat.addView(textView);
            textViewList.add(textView);
        }

        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = itemLayoutList.get(layoutCompat) - 1;
                if (getSelected() == pos) return;

                if (getTabChangeListener() != null)
                    getTabChangeListener().onTabChanged(pos);
                setSelected(pos);
            }
        });

        layoutCompat.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        itemLayoutList.put(layoutCompat, itemLayoutList.size() + 1);
        addView(layoutCompat);

    }

    /**
     * @param title
     * @return
     */
    @SuppressLint("WrongConstant")
    private TextView addText(final String title) {
        final TextView textView = new TextView(getContext());
        textView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
                param.gravity = Gravity.CENTER;
                textView.setGravity(Gravity.CENTER);
                textView.setText(title);
                textView.setTextColor(getInActiveColor());
                if (getPathFont() != null)
                    Font.fromAsset(getContext(), getPathFont(), getTypeface(), textView);

            }
        });
        return textView;
    }

    /**
     * @param icon
     * @return
     */
    private ImageView addImage(final int icon) {
        final ImageView image = new ImageView(getContext());
        image.setTag(icon);
        image.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
                image.setLayoutParams(param);
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setImageResource(icon);
            }
        });
        return image;
    }

    /**
     * @param color
     */
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        this.backgroundColor = color;
    }

    /**
     * @return
     */
    private TabChangeListener getTabChangeListener() {
        return tabChangeListener;
    }

    /**
     * @param tabChangeListener
     */
    public void setTabChangeListener(TabChangeListener tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
    }

    /**
     * @return
     */
    public int getSelected() {
        return selected;
    }

    /**
     * @param selected
     */
    public void setSelected(int selected) {
        this.selected = selected;
        animation();
    }

    /**
     * @return
     */
    private BottomTab.EnableType getEnableType() {
        return enableType == null ? enableType = BottomTab.EnableType.NO_ANIMATION : enableType;
    }

    /**
     * @param enableType
     */
    public void setEnableType(BottomTab.EnableType enableType) {
        this.enableType = enableType;
    }

    /**
     * @return
     */
    public int getActiveColor() {
        return activeColor;
    }

    /**
     * @param activeColor
     */
    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }

    /**
     * @return
     */
    public int getInActiveColor() {
        return inActiveColor;
    }

    /**
     * @param inActiveColor
     */
    public void setInActiveColor(int inActiveColor) {
        this.inActiveColor = inActiveColor;
    }

    /**
     * @param index           add badge to index of bottomTab
     * @param backgroundColor change badge backgroundColor
     * @param textColor       change badge textColor
     * @param number          change badge number
     */
    public void addBadge(int index, int backgroundColor, int textColor, int number) {
        if (badgeList.get(index) == null) {
            View view = getChildAt(index);
            BadgeView badgeView = new BadgeView(getContext());
            badgeView.setParentView(view);
            badgeView.setFont(getPathFont(), getTypeface());
            badgeView.setBadgeColor(backgroundColor, textColor);
            badgeView.setNumber(number);
            badgeView.setBadgePosition(BadgeView.POSITION_CENTER_TOP);
            badgeView.show();
            ViewHelper.setMargins(getContext(), badgeView, (8), (2), (0), (0));
            badgeList.put(index, badgeView);
        }
    }

    /**
     * @param index remove badge from index of bottomTab
     */
    public void removeBadge(int index) {
        if (badgeList.get(index) != null) {
            badgeList.get(index).hide();
            badgeList.remove(index);
        }
    }

    /**
     *
     */
    public interface TabChangeListener {
        void onTabChanged(int index);
    }
}
