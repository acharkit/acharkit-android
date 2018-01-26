package ir.acharkit.android.util.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;

import ir.acharkit.android.util.Log;
import ir.acharkit.android.util.Util;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/24/2017
 * Email:   alirezat775@gmail.com
 */
public class IntentHelper {

    private static final String TAG = IntentHelper.class.getName();

    /**
     * @param context
     * @param phoneNumber
     * @param message
     */
    @RequiresPermission(Manifest.permission.SEND_SMS)
    public static void smsIntent(@NonNull Context context, @Size(min = 1) int phoneNumber, String message) {
        context.startActivity(new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("sms:" + phoneNumber))
                .putExtra("sms_body", message)
                .putExtra("address", phoneNumber)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * @param context
     * @param phoneNumber
     */
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static void callIntent(@NonNull Context context, @Size(min = 1) int phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * @param context
     * @param url
     */
    public static void browserIntent(@NonNull Context context, @NonNull String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(url))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * @param context
     * @param to
     * @param subject
     * @param body
     */
    public static void emailIntent(@NonNull Context context, @NonNull String to, @NonNull String subject, @NonNull String body) {
        if (!Util.isValidateEmail(to)) {
            Log.d(TAG, "invalid mail");
            return;
        }
        StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(to));
        builder.append("?subject=").append(subject);
        builder.append("&body=").append(body);
        String uri = builder.toString();
        context.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(uri))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
