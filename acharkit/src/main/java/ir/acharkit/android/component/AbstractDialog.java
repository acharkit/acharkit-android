package ir.acharkit.android.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;

import ir.acharkit.android.annotation.FontType;
import ir.acharkit.android.annotation.LinearLayoutOrientation;
import ir.acharkit.android.annotation.ViewGravity;
import ir.acharkit.android.util.Font;
import ir.acharkit.android.util.helper.ConvertHelper;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class AbstractDialog {

    private static final String TAG = Builder.class.getName();
    private final Builder builder;

    /**
     * @param builder
     */
    public AbstractDialog(Builder builder) {
        this.builder = builder;
    }

    public static class Builder {
        private HashMap<Integer, View> views = new HashMap<>();
        private LinearLayout dialogView;
        private Dialog dialog;
        private LinearLayout buttonView;
        private Context context;

        private String title;
        private String message;
        private String textButton;

        private int backgroundColor;
        private int backgroundResource;
        private int textColor;
        private int buttonTextColor;
        private int buttonBackgroundColor;

        private int typeface;
        private String pathFont;

        /**
         * @param context
         */
        public Builder(Context context) {
            this.context = context;
            init();
        }

        /**
         * @param pathFont
         * @param typeface
         * @return
         */
        public Builder setFont(@NonNull String pathFont, @FontType int typeface) {
            this.typeface = typeface;
            this.pathFont = pathFont;
            return this;
        }

        /**
         * @param pathFont
         * @return
         */
        public Builder setFont(@NonNull String pathFont) {
            this.typeface = Typeface.NORMAL;
            this.pathFont = pathFont;
            return this;
        }


        public Builder setSize(float width, float height) {
            if (width > 1 || height > 1 || width <= 0 || height <= 0) {
                throw new NumberFormatException("The number entered must be less than 1 and greater than 0");
            }
            int widthDialog = (int) (context.getResources().getDisplayMetrics().widthPixels * width);
            int heightDialog = (int) (context.getResources().getDisplayMetrics().heightPixels * height);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(widthDialog, heightDialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            return this;
        }

        /**
         * @param backgroundColor
         * @return
         */
        public Builder setBackgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            getDialogView().setBackgroundColor((backgroundColor == 0) ? 0xFF2D2D2D : backgroundColor);
            return this;
        }

        /**
         * @param backgroundResource
         * @return
         */
        public Builder setBackgroundResource(@DrawableRes int backgroundResource) {
            this.backgroundResource = backgroundResource;
            getDialogView().setBackgroundResource(backgroundResource);
            return this;
        }

        /**
         * @param backgroundColor
         * @param cornerRadius
         * @return
         */
        public Builder setBackgroundColor(@ColorInt int backgroundColor, int cornerRadius) {
            this.backgroundColor = backgroundColor;
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(ConvertHelper.dpToPx(context, cornerRadius));
            shape.setColor((backgroundColor == 0) ? 0xFF2D2D2D : backgroundColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getDialogView().setBackground(shape);
            } else {
                getDialogView().setBackgroundColor((backgroundColor == 0) ? 0xFF2D2D2D : backgroundColor);
            }
            return this;
        }

        /**
         * @return
         */
        private Builder init() {
            dialogView = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            layoutParams.weight = 1f;
            getDialogView().setLayoutParams(layoutParams);
            getDialogView().setBackgroundColor((backgroundColor == 0) ? 0xFF2D2D2D : backgroundColor);
            getDialogView().setGravity(Gravity.CENTER);
            getDialogView().setOrientation(LinearLayout.VERTICAL);
            dialog = new Dialog(context);

            dialog.setContentView(getDialogView(), layoutParams);

            int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.80);
            int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.80);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            return this;
        }

        /**
         * @return
         */
        private LinearLayout getDialogView() {
            return dialogView;
        }

        /**
         * @param view
         * @return
         */
        private void setViewList(int index, View view) {
            views.put(index, view);
        }


        /**
         *
         */
        private void addViews() {
            TreeMap<Integer, View> sortView = new TreeMap<>(views);
            for (Object view : sortView.keySet()) {
                getDialogView().addView(sortView.get(view));
            }
        }

        /**
         * @param title
         * @param textSize
         * @param textColor
         * @return
         */
        public Builder setTitle(@Size(min = 2) String title, int textSize, @ColorInt int textColor) {
            setTitle(title, textSize, textColor, Gravity.CENTER);
            return this;
        }

        /**
         * @param title
         * @param textColor
         * @param gravity
         * @return
         */
        public Builder setTitle(@Size(min = 2) String title, int textSize, @ColorInt int textColor, @ViewGravity int gravity) {
            this.title = title;
            this.textColor = textColor;
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            textView.setLayoutParams(params);
            ViewHelper.setMargins(context, textView, 15, 15, 15, 15);
            Font.fromAsset(context, getPathFont(), (getTypeface() == 0) ? Typeface.NORMAL : typeface, textView);
            textView.setText(title);
            textView.setTextSize(ConvertHelper.dpToPx(context, textSize));
            textView.setTextColor((textColor == 0) ? 0xFFFFFFFF : textColor);
            textView.setGravity(gravity);
            setViewList(0, textView);
            return this;
        }

        /**
         * @param message
         * @param textSize
         * @param textColor
         * @return
         */
        public Builder setMessage(@Size(min = 2) String message, int textSize, @ColorInt int textColor) {
            setMessage(message, textSize, textColor, Gravity.CENTER);
            return this;
        }

        /**
         * @param message
         * @param textColor
         * @param gravity
         * @return
         */
        public Builder setMessage(@Size(min = 2) String message, int textSize, @ColorInt int textColor, @ViewGravity int gravity) {
            this.message = message;
            this.textColor = textColor;
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            textView.setLayoutParams(params);
            ViewHelper.setMargins(context, textView, 15, 15, 15, 15);
            Font.fromAsset(context, getPathFont(), (getTypeface() == 0) ? Typeface.NORMAL : typeface, textView);
            textView.setText(message);
            textView.setTextSize(ConvertHelper.dpToPx(context, textSize));
            textView.setTextColor((textColor == 0) ? 0xFFFFFFFF : textColor);
            textView.setGravity(gravity);
            setViewList(1, textView);

            return this;
        }

        /**
         * @return
         */
        private LinearLayout getRootViewButton() {
            return buttonView;
        }

        /**
         * @param orientation
         * @return
         */
        public Builder setButtonsViewOrientation(@LinearLayoutOrientation int orientation) {
            buttonView = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1.0f;
            getRootViewButton().setLayoutParams(layoutParams);
            getRootViewButton().setOrientation(orientation);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @param clickListener
         * @return
         */
        public Builder addButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor, View.OnClickListener clickListener) {
            addButton(textButton, textSize, buttonBackgroundColor, buttonTextColor, clickListener, false, Gravity.CENTER, 0);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @param clickListener
         * @param gravity
         * @return
         */
        public Builder addButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor, View.OnClickListener clickListener, @ViewGravity int gravity) {
            addButton(textButton, textSize, buttonBackgroundColor, buttonTextColor, clickListener, false, gravity, 0);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @param clickListener
         * @param gravity
         * @param cornerRadius
         * @return
         */
        public Builder addButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor, View.OnClickListener clickListener, @ViewGravity int gravity, int cornerRadius) {
            addButton(textButton, textSize, buttonBackgroundColor, buttonTextColor, clickListener, false, gravity, cornerRadius);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @return
         */
        public Builder addDismissButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor) {
            addButton(textButton, textSize, buttonBackgroundColor, buttonTextColor, null, true, Gravity.CENTER, 0);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @param gravity
         * @return
         */
        public Builder addDismissButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor, @ViewGravity int gravity) {
            addButton(textButton, textSize, buttonBackgroundColor, buttonTextColor, null, true, gravity, 0);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @param gravity
         * @param cornerRadius
         * @return
         */
        public Builder addDismissButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor, @ViewGravity int gravity, int cornerRadius) {
            addButton(textButton, textSize, buttonBackgroundColor, buttonTextColor, null, true, gravity, cornerRadius);
            return this;
        }

        /**
         * @param textButton
         * @param textSize
         * @param buttonBackgroundColor
         * @param buttonTextColor
         * @param clickListener
         * @param dismiss
         * @param gravity
         * @param cornerRadius
         * @return
         */
        private Builder addButton(@Size(min = 2) String textButton, int textSize, @ColorInt int buttonBackgroundColor, @ColorInt int buttonTextColor, View.OnClickListener clickListener, boolean dismiss, @ViewGravity int gravity, int cornerRadius) {
            this.textButton = textButton;
            this.buttonBackgroundColor = buttonBackgroundColor;
            this.buttonTextColor = buttonTextColor;

            Button button = new Button(context);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            button.setLayoutParams(params);

            ViewHelper.setMargins(context, button, 15, 15, 15, 15);
            Font.fromAsset(context, getPathFont(), (getTypeface() == 0) ? Typeface.NORMAL : typeface, button);
            button.setText(textButton);
            button.setTextSize(ConvertHelper.dpToPx(context, textSize));
            button.setTextColor((buttonTextColor == 0) ? 0xFFFFFFFF : buttonTextColor);
            if (cornerRadius != 0) {
                GradientDrawable shape = new GradientDrawable();
                shape.setCornerRadius(ConvertHelper.dpToPx(context, cornerRadius));
                shape.setColor((buttonBackgroundColor == 0) ? 0xFF2D2D2D : buttonBackgroundColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    button.setBackground(shape);
                } else {
                    button.setBackgroundColor((buttonBackgroundColor == 0) ? 0xFF2D2D2D : buttonBackgroundColor);
                }
            } else {
                button.setBackgroundColor(buttonBackgroundColor);
            }
            button.setGravity(gravity);
            if (dismiss) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            } else {
                button.setOnClickListener(clickListener);
            }
            if (getRootViewButton() != null) {
                ViewGroup parent = (ViewGroup) getRootViewButton().getParent();
                if (parent != null) {
                    parent.removeView(getRootViewButton());
                }
            }
            try {
                getRootViewButton().addView(button);
                setViewList(3, getRootViewButton());
            } catch (NullPointerException e) {
                throw new NullPointerException("must be called first 'setButtonsViewOrientation()' before add buttons");
            }


            return this;
        }

        /**
         * @return
         */
        public Builder setProgressbar(View progressbar) {
            setViewList(2, progressbar);
            return this;
        }

        /**
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        /**
         * @param cancelable
         * @return
         */
        public Builder setCanceledOnTouchOutside(boolean cancelable) {
            dialog.setCanceledOnTouchOutside(cancelable);
            return this;
        }

        /**
         * @param listener
         * @return
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener listener) {
            dialog.setOnDismissListener(listener);
            return this;
        }

        /**
         * @param listener
         * @return
         */
        public Builder setOnCancelListener(DialogInterface.OnCancelListener listener) {
            dialog.setOnCancelListener(listener);
            return this;
        }

        /**
         * @return
         */
        public Builder show() {
            addViews();
            dialog.show();
            return new Builder(context);
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
    }
}
