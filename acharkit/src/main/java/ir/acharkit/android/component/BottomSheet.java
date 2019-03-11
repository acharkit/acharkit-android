package ir.acharkit.android.component;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import ir.acharkit.android.component.bottomSheet.BottomSheetView;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    5/17/18
 * Email:   alirezat775@gmail.com
 */
public class BottomSheet {

    private static final String TAG = BottomSheet.class.getName();
    private static final int ANIMATION_DURATION = 300;
    private AppCompatActivity activity;
    private BottomSheetView bottomSheetView;
    private View view;
    private Callback callback;

    /**
     * @param activity
     * @param bottomSheetId
     */
    public BottomSheet(AppCompatActivity activity, @IdRes int bottomSheetId) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, bottomSheetId);
    }

    /**
     * @param activity
     * @param view
     * @param bottomSheetId
     */
    public BottomSheet(AppCompatActivity activity, View view, @IdRes int bottomSheetId) {
        init(activity, view, bottomSheetId);
    }

    /**
     * @param activity
     * @param view
     * @param bottomSheetId
     */
    private void init(final AppCompatActivity activity, View view, @IdRes final int bottomSheetId) {
        this.activity = activity;
        this.view = view;
        bottomSheetView = view.findViewById(bottomSheetId);
    }

    /**
     * @param height
     */
    public void setHeight(float height) {
        if (bottomSheetView != null) {
            float width = 0f;
            if (ViewHelper.isPortrait(activity))
                width = ViewHelper.getScreenWidth() > 1080 ? 0.5f : 1f;
            else if (ViewHelper.isLandscape(activity))
                width = 0.5f;

            bottomSheetView.setSize(width, height);
        }
    }

    /**
     * @param color
     */
    public void setBackgroundColor(@ColorInt int color) {
        if (bottomSheetView != null)
            bottomSheetView.setBottomSheetColor(color);
    }

    /**
     * show bottomSheet
     */
    public void show() {
        if (bottomSheetView != null) {
            bottomSheetView.show();
        }
    }

    /**
     * collapse bottomSheet
     */
    public void collapse() {
        if (bottomSheetView != null) {
            bottomSheetView.collapse();
        }
    }

    /**
     * @param callback listener for change state bottomSheet
     */
    public void setStateChangeListener(Callback callback) {
        bottomSheetView.setCallback(callback);
    }

    public interface Callback {
        void collapse();

        void show();
    }
}
