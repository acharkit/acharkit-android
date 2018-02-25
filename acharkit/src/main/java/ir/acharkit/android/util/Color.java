package ir.acharkit.android.util;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/22/18
 * Email:   alirezat775@gmail.com
 */
public class Color {

    public static int lighten(int color, double fraction) {
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = android.graphics.Color.alpha(color);
        return android.graphics.Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = android.graphics.Color.alpha(color);

        return android.graphics.Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int) Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }
}
