package ir.acharkit.android.util;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.acharkit.android.annotation.FontType;
import ir.acharkit.android.annotation.ToastDuration;
import ir.acharkit.android.util.helper.ConvertHelper;
import ir.acharkit.android.util.helper.StringHelper;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class Util {

    //    https://regex101.com/r/0VTTem/2
    public static final String REGEX_PHONE_NUMBER_IRAN = "^[^\\d]*(?:|0|0098|\\+98|98)((9\\d{2})\\d{7})[^\\d]*$";
    private static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String TAG = Util.class.getName();

    /**
     * @param email
     * @return validate email address
     */
    public static boolean isValidateEmail(@NonNull String email) {
        if (StringHelper.isNotEmpty(email)) {
            Pattern pattern = Pattern.compile(REGEX_EMAIL);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } else {
            return false;
        }
    }


    /**
     * @param inputNumber
     * @return validate phone number (specific for iran number)
     */
    public static String isValidPhoneNumberIran(@NonNull String inputNumber) {
        String res = "";
        Pattern p = Pattern.compile(REGEX_PHONE_NUMBER_IRAN);
        Matcher m = p.matcher(inputNumber);
        if (!m.find()) {
            return null;
        }
        res = m.toMatchResult().group(1);
        return arabicToDecimal(res);
    }

    /**
     * @param number arabic or persian number
     * @return convert arabic or persian number to decimal number
     */
    public static String arabicToDecimal(@NonNull String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    /**
     * @param context
     * @param message
     * @param duration
     * @param background
     * @param messageColor
     * @param typeface
     * @param path
     */
    public static void showToast(@NonNull Context context, @NonNull String message, @ToastDuration int duration, @ColorInt int background, @ColorInt int messageColor, @NonNull String path, @FontType int typeface) {
        LinearLayout toastView = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        toastView.setLayoutParams(layoutParams);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(ConvertHelper.dpToPx(context, 15));
        shape.setColor((background == 0) ? 0xFF2D2D2D : background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toastView.setBackground(shape);
        } else {
            toastView.setBackgroundColor((background == 0) ? 0xFF2D2D2D : background);
        }
        toastView.setGravity(Gravity.CENTER);
        toastView.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        ViewHelper.setMargins(context, textView, 15, 10, 15, 10);
        Font.fromAsset(context, path, typeface, textView);
        textView.setText(message);
        textView.setTextColor((messageColor == 0) ? 0xFFFFFFFF : messageColor);

        toastView.addView(textView);

        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 100);
        toast.setDuration(duration);
        toast.show();
    }

    /**
     * @param context
     * @param message
     * @param duration
     */
    public static void showToast(@NonNull Context context, @NonNull String message, @ToastDuration int duration) {
        showToast(context, message, duration, 0, 0, "", Typeface.NORMAL);
    }

    /**
     * @param context
     * @param message
     * @param duration
     * @param typeface
     * @param path
     */
    public static void showToast(@NonNull Context context, @NonNull String message, @ToastDuration int duration, @NonNull String path, @FontType int typeface) {
        showToast(context, message, duration, 0, 0, path, typeface);
    }

    /**
     * @param context
     * @param message
     * @param duration
     * @param background
     * @param messageColor
     */
    public static void showToast(@NonNull Context context, @NonNull String message, @ToastDuration int duration, @ColorInt int background, @ColorInt int messageColor) {
        showToast(context, message, duration, background, messageColor, "", Typeface.NORMAL);
    }

    /**
     * @param context
     * @param PackageName
     * @return
     */
    public static boolean isPackageInstalled(Context context, String PackageName) {
        PackageManager manager = context.getPackageManager();
        boolean isAppInstalled = false;
        try {
            manager.getPackageInfo(PackageName, PackageManager.GET_ACTIVITIES);
            isAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return isAppInstalled;
    }

    /**
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return false;
    }

    /**
     * @param context
     * @param label
     * @param content
     */
    public static void copyToClipboard(Context context, String label, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, content);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * @param is
     * @param os
     */
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 8192;
        try {
            byte[] bytes = new byte[buffer_size];
            while (true) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }
}
