package ir.acharkit.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Author:      Alireza Mahmoodi
 * Created:     9/23/2016
 * Email:       mahmoodi.dev@gmail.com
 * Website:     alirezamh.com
 */

public class Cache {

    private static SharedPreferences sharedPreferences;
    private static String preferences = null;
    private static Context context;

    /**
     * @return
     */
    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getContext().getSharedPreferences(preferences, Activity.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * @return
     */
    public static Context getContext() {
        return context;
    }

    /**
     * @param context
     */
    public static void setContext(@NonNull Context context) {
        Cache.context = context;
    }

    /**
     * @param key
     * @param value
     */
    public static void put(@NonNull String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void put(@NonNull String key, float value) {
        getSharedPreferences().edit().putFloat(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void put(@NonNull String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void put(@NonNull String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void put(@NonNull String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    /**
     * @param key
     * @param value
     */
    public static void put(@NonNull String key, Set<String> value) {
        getSharedPreferences().edit().putStringSet(key, value).apply();
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static String get(@NonNull String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static int get(@NonNull String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static long get(@NonNull String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static float get(@NonNull String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean get(@NonNull String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static Set<String> get(@NonNull String key, Set<String> defaultValue) {
        return getSharedPreferences().getStringSet(key, defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public static boolean has(@NonNull String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * @param key
     * @return
     */
    public static boolean remove(@NonNull String key) {
        return getSharedPreferences().edit().remove(key).commit();
    }
}
