package ir.acharkit.android.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ir.acharkit.android.R;
import ir.acharkit.android.util.Log;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    1/26/18
 * Email:   alirezat775@gmail.com
 */
public class GifView extends View {

    public static final int INFINITE = -1;
    private static final int DEFAULT_DURATION = 1000; // ms
    private static final String TAG = GifView.class.getName();
    private Movie gif;
    private long startTime = 0;
    private int currentTime = 0;
    private int repeatCount;
    private boolean repeated = true;
    private boolean fullScreen;

    private int gifViewWidth;
    private int gifViewHeight;


    /**
     * @param context current context, will be used to access resources
     * @param attrs   repeatCount and src for load resource
     */
    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GifView, 0, 0);
        try {
            setGifViewWidth(attributes.getInteger(R.styleable.GifView_gifWidth, 0));
            setGifViewHeight(attributes.getInteger(R.styleable.GifView_gifHeight, 0));
            setRepeatCount(attributes.getInteger(R.styleable.GifView_repeatCount, INFINITE));
            loadResource(attributes.getResourceId(R.styleable.GifView_src, 0));
        } finally {
            attributes.recycle();
        }
    }

    /**
     * @param id gif file id from resources
     * @return
     */
    public GifView loadResource(int id) {
        gif = Movie.decodeStream(getResources().openRawResource(id));
        requestLayout();
        return this;
    }

    /**
     * @param path gif file path from storage
     * @return
     */
    public GifView loadFile(String path) {
        File file = new File(path);
        try {
            gif = Movie.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.w(TAG, e);
        }
        requestLayout();
        return this;
    }

    /**
     * @param file gif file from storage
     * @return
     */
    public GifView loadFile(File file) {
        try {
            gif = Movie.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.w(TAG, e);
        }
        requestLayout();
        return this;
    }

    /**
     * @param stream load gif from InputStream
     * @return
     */
    public GifView loadStream(InputStream stream) {
        gif = Movie.decodeStream(stream);
        requestLayout();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (gif != null) {
            if (isFullScreen())
                setMeasuredDimension(ViewHelper.getScreenWidth(), ViewHelper.getScreenHeight());
            else setMeasuredDimension(getGifViewWidth(), getGifViewHeight());
        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (gif != null) {
            if (isFullScreen())
                canvas.scale((float) this.getWidth() / (float) getGifViewWidth(), (float) this.getHeight() / (float) getGifViewHeight());

            if (getGifViewHeight() != 0 && getGifViewWidth() != 0)
                canvas.scale((float) this.getGifViewWidth() / (float) gif.width(), (float) this.getGifViewHeight() / (float) gif.height());

            update();
            drawGif(canvas);
            invalidate();
        } else {
            drawGif(canvas);
        }
    }

    /**
     * update frame gif
     */
    private void update() {
        if (repeated || getRepeatCount() == INFINITE) {
            long now = System.currentTimeMillis();
            if (startTime == 0) {
                startTime = now;
            }
            int dur = gif.duration();
            if (dur == 0) {
                dur = DEFAULT_DURATION;
            }
            currentTime = (int) ((now - startTime) % dur);

            if (getRepeatCount() != INFINITE) {
                int durCount = (dur * getRepeatCount());
                if ((startTime + durCount) < System.currentTimeMillis()) repeated = false;
            }
        }
    }

    /**
     * @param canvas drawing gif
     */
    private void drawGif(Canvas canvas) {
        gif.setTime(currentTime);
        gif.draw(canvas, 0, 0);

    }

    /**
     * @return repeat count
     */
    private int getRepeatCount() {
        return repeatCount == 0 ? INFINITE : repeatCount;
    }

    /**
     * @param repeatCount int for repeat gif animation
     * @return
     */
    public GifView setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    /**
     * @return width and height fix match screen
     */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /**
     * @param fullScreen enable or disable width and height fix match screen
     * @return
     */
    public GifView setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        requestLayout();
        return this;
    }

    /**
     * @return
     */
    private int getGifViewWidth() {
        if (gifViewWidth == 0) {
            gifViewWidth = gif != null ? gif.width() : 0;
        }
        return gifViewWidth;
    }

    /**
     * @param gifViewWidth
     * @return
     */
    public GifView setGifViewWidth(int gifViewWidth) {
        this.gifViewWidth = gifViewWidth;
        requestLayout();
        return this;
    }

    /**
     * @return
     */
    private int getGifViewHeight() {
        if (gifViewHeight == 0) {
            gifViewHeight = gif != null ? gif.height() : 0;
        }
        return gifViewHeight;
    }

    /**
     * @param gifViewHeight
     * @return
     */
    public GifView setGifViewHeight(int gifViewHeight) {
        this.gifViewHeight = gifViewHeight;
        requestLayout();
        return this;
    }

}
