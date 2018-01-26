package ir.acharkit.android.util;

import android.support.annotation.NonNull;

import ir.acharkit.android.BuildConfig;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    19/09/2017
 * Email:   alirezat775@gmail.com
 */
public class Log {

    private static boolean debugMode = false;
    private static boolean acharkitLog = false;


    public static boolean getAcharkitLog() {
        return acharkitLog;
    }

    public static void setAcharkitLog(boolean acharkitLog) {
        Log.acharkitLog = acharkitLog;
    }


    /**
     * @return
     */
    public static boolean getDebugMode() {
        return debugMode;
    }

    /**
     * @param debugMode
     */
    public static void setDebugMode(boolean debugMode) {
        Log.debugMode = debugMode;
    }

    /**
     * @param tag
     * @param message
     */
    public static void d(@NonNull String tag, String message) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.d(tag, message);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.d(tag, message);
                }
            }
        }
    }

    /**
     * @param tag
     * @param message
     */
    public static void i(@NonNull String tag, String message) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.i(tag, message);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.i(tag, message);
                }
            }
        }
    }

    /**
     * @param tag
     * @param throwable
     * @param message
     */
    static public void e(@NonNull String tag, Throwable throwable, String message) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.e(tag, message, throwable);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.e(tag, message, throwable);
                }
            }
        }
    }

    /**
     * @param tag
     * @param throwable
     * @param message
     */
    static public void wtf(@NonNull String tag, Throwable throwable, String message) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.wtf(tag, message, throwable);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.wtf(tag, message, throwable);
                }
            }
        }
    }

    /**
     * @param tag
     * @param throwable
     */
    static public void wtf(@NonNull String tag, Throwable throwable) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.wtf(tag, throwable);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.wtf(tag, throwable);
                }
            }
        }
    }

    /**
     * @param tag
     * @param throwable
     */
    static public void w(@NonNull String tag, Throwable throwable) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.w(tag, throwable);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.w(tag, throwable);
                }
            }
        }
    }

    /**
     * @param tag
     * @param throwable
     * @param message
     * @param args
     */
    static public void e(@NonNull String tag, Throwable throwable, String message, Object args) {
        if (Log.getDebugMode()) {
            if (Log.getAcharkitLog()) {
                android.util.Log.e(tag, String.format(message, args), throwable);
            } else {
                if (!tag.contains(BuildConfig.APPLICATION_ID)) {
                    android.util.Log.e(tag, String.format(message, args), throwable);
                }
            }
        }
    }
}
