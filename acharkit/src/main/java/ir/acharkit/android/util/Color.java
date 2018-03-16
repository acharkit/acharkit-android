package ir.acharkit.android.util;


import android.support.annotation.Size;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/22/18
 * Email:   alirezat775@gmail.com
 */
public class Color {

    public static final int BLACK = 0xFF000000;
    public static final int DKGRAY = 0xFF444444;
    public static final int GRAY = 0xFF888888;
    public static final int LTGRAY = 0xFFCCCCCC;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int RED = 0xFFFF0000;
    public static final int GREEN = 0xFF00FF00;
    public static final int BLUE = 0xFF0000FF;
    public static final int YELLOW = 0xFFFFFF00;
    public static final int CYAN = 0xFF00FFFF;
    public static final int MAGENTA = 0xFFFF00FF;
    public static final int TRANSPARENT = 0;

    /**
     * @param color
     * @param fraction
     * @return
     */
    public static int lighten(int color, @Size(min = 0, max = 1) double fraction) {
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = android.graphics.Color.alpha(color);
        return android.graphics.Color.argb(alpha, red, green, blue);
    }

    /**
     * @param color
     * @param fraction
     * @return
     */
    public static int darken(int color, @Size(min = 0, max = 1) double fraction) {
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = android.graphics.Color.alpha(color);
        return android.graphics.Color.argb(alpha, red, green, blue);
    }

    /**
     * @param color
     * @param fraction
     * @return
     */
    private static int darkenColor(int color, double fraction) {
        return (int) Math.max(color - (color * fraction), 0);
    }

    /**
     * @param color
     * @param fraction
     * @return
     */
    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }
}
