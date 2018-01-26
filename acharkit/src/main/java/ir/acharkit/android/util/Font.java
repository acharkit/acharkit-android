package ir.acharkit.android.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.acharkit.android.annotation.FontType;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class Font {

    private static final String TAG = Font.class.getName();

    /**
     * @param typeface
     * @param path
     * @param view
     */
    public static void fromAsset(Context context, @NonNull String path, @FontType int typeface, @NonNull View... view) {
        Typeface font = null;
        try {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/" + path);
        } catch (RuntimeException e) {
            Log.w(TAG, e);
        }
        setFont(font, typeface, view);
    }

    /**
     * @param typeface
     * @param font
     * @param view
     */
    public static void setFont(@NonNull Typeface font, @FontType int typeface, @NonNull View... view) {
        if (font == null) return;
        for (int i = 0; i < view.length; i++) {
            ((TextView) view[i]).setTypeface(font, typeface);
        }
    }

    /**
     * @param viewGroup
     * @param context
     * @param path
     * @param typeface
     */
    public static void setFontViewGroup(ViewGroup viewGroup, Context context, @NonNull String path, @FontType int typeface) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                setFontViewGroup((ViewGroup) child, context, path, typeface);
                continue;
            }

            if (child instanceof TextView) {
                String tag = (String) child.getTag();
                if (tag != null && tag.equalsIgnoreCase("icon")) {
                    fromAsset(context, path, typeface, (TextView) child);
                } else {
                    fromAsset(context, path, typeface, (TextView) child);
                }
            }
        }
    }
}
