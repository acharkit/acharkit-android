package ir.acharkit.android.util.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.io.UnsupportedEncodingException;

import ir.acharkit.android.util.Logger;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/24/2017
 * Email:   alirezat775@gmail.com
 */
public class ConvertHelper {

    private static final String TAG = ConvertHelper.class.getName();

    /**
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(@NonNull Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    /**
     * @param context
     * @param px
     * @return
     */
    public static int pixelsToDp(@NonNull Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }

    /**
     * @param context
     * @param sp
     * @return
     */
    public static int spToPx(@NonNull Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * @param context
     * @param px
     * @return
     */
    public static int pxToSp(@NonNull Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * @param data
     * @return
     */
    public static String byteToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * @param str
     * @return
     */
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);

            }
            return buffer;
        }
    }

    /**
     * @param data
     * @return
     */
    public static String byteToString(byte[] data) {
        String string = null;
        try {
            string = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.w(TAG, e);
        }
        return string;
    }

    /**
     * @param data
     * @return
     */
    public static byte[] stringToByte(String data) {
        byte[] bytes = null;
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.w(TAG, e);
        }
        return bytes;
    }

    /**
     * @param input
     * @return
     */
    public static byte[] hexStringToByteArray(String input) {
        int len = input.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i + 1), 16));
        }
        return data;
    }
}
