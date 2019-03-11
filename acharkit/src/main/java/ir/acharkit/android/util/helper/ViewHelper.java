package ir.acharkit.android.util.helper;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/24/2017
 * Email:   alirezat775@gmail.com
 */
public class ViewHelper {

    private static final String TAG = ViewHelper.class.getName();

    /**
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setMargins(@NonNull Context context, @NonNull View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() == null) {
            throw new NullPointerException("view.getLayoutParams()' on a null object reference");
        } else if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(ConvertHelper.dpToPx(context, left), ConvertHelper.dpToPx(context, top), ConvertHelper.dpToPx(context, right), ConvertHelper.dpToPx(context, bottom));
            view.requestLayout();
        }
    }

    /**
     * @param activity
     * @return
     */
    public static int[] getScreenSize(@NonNull AppCompatActivity activity) {
        Point size = new Point();
        WindowManager w = activity.getWindowManager();
        w.getDefaultDisplay().getSize(size);
        return new int[]{size.x, size.y};
    }

    /**
     * @param activity
     * @param status
     */
    public static void setOrientation(@NonNull AppCompatActivity activity, boolean status) {
        if (status) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            }
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }
    }

    /**
     * @param view
     * @param context
     */
    public static void hideKeyboard(@NonNull View view, @NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * @param view
     * @param context
     */
    public static void showKeyboard(@NonNull View view, @NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * @param activity
     * @return
     */
    public static boolean isTablet(@NonNull AppCompatActivity activity) {
        return (activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * @return
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * @param activity
     */
    public static void setFullScreen(@NonNull final AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * @param activity
     */
    public static void setLandscape(@NonNull AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    /**
     * @param activity
     */
    public static void setPortrait(@NonNull AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * @param activity
     */
    public static boolean isLandscape(@NonNull AppCompatActivity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    /**
     * @param activity
     */
    public static boolean isPortrait(@NonNull AppCompatActivity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * @param activity
     * @return
     */
    public static Bitmap screenShot(@NonNull AppCompatActivity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, getScreenWidth(), getScreenHeight());
        decorView.destroyDrawingCache();
        return ret;
    }

    /**
     * @param activity
     * @return
     */
    public static boolean isScreenLock(@NonNull AppCompatActivity activity) {
        KeyguardManager km = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * @param activity
     * @param duration
     */
    @RequiresPermission(allOf = {Manifest.permission.WRITE_SECURE_SETTINGS, Manifest.permission.WRITE_SETTINGS})
    public static void setSleepDuration(@NonNull AppCompatActivity activity, final int duration) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
    }

    /**
     * @param activity
     * @return
     */
    public static int getSleepDuration(@NonNull AppCompatActivity activity) {
        try {
            return Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }

    /**
     * @param activity
     * @return
     */
    public static Bitmap screenShotFullView(@NonNull AppCompatActivity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return b;
    }
}
