package ir.acharkit.android.component;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.Size;
import android.view.View;

import ir.acharkit.android.component.progress.AbstractProgress;
import ir.acharkit.android.util.Color;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/16/18
 * Email:   alirezat775@gmail.com
 */


public class Progress {

    public static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_TIME_DURATION = 500;
    private AbstractProgress progress;
    private Context context;
    private int timeDuration;
    private int color;

    /**
     * @param context current context, will be used to access resources
     */
    public Progress(Context context) {
        this.context = context;
    }

    /**
     * @return progress subClass AbstractProgress
     */
    protected AbstractProgress getProgress() {
        if (progress != null) return progress;
        else throw new NullPointerException("Progress is Null");
    }

    /**
     * @param progress instance of AbstractProgress
     * @return
     */
    @CheckResult
    public Progress setProgress(AbstractProgress progress) {
        this.progress = progress;
        return this;
    }

    /**
     * @return color progress
     */
    private int getColor() {
        return color == 0 ? color = DEFAULT_COLOR : color;
    }

    /**
     * @param color change color progress
     * @return
     */
    @CheckResult
    public Progress setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * @return time duration progress
     */
    private int getTimeDuration() {
        return timeDuration == 0 ? timeDuration = DEFAULT_TIME_DURATION : timeDuration;
    }

    /**
     * @param timeDuration time duration progress
     * @return
     */
    private Progress setTimeDuration(@Size(min = 100) int timeDuration) {
        this.timeDuration = timeDuration;
        return this;
    }

    /**
     * load progress
     */
    public void load() {
        progress.changeLayout(3, getColor(), getTimeDuration());
    }

    /**
     * hide(GONE) and stop animate progress
     * recommended use this instead changeVisibility
     */
    public void hide() {
        getProgress().setVisibility(View.GONE);
        getProgress().animator().end();
    }

    /**
     * show(VISIBLE) and start animate progress
     * recommended use this instead changeVisibility
     */
    public void show() {
        getProgress().setVisibility(View.VISIBLE);
        getProgress().animator().start();
    }

    private Context getContext() {
        return context;
    }
}
