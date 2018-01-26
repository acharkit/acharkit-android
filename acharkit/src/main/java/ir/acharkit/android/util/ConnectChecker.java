package ir.acharkit.android.util;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    17/09/2017
 * Email:   alirezat775@gmail.com
 */
public class ConnectChecker {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;

    /**
     * @param context
     * @return boolean
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET})
    public static boolean isInternetAvailable(Context context) {
        boolean isConnected;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }


    /**
     * @param context
     * @return int
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET})
    public static int connectionType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return 0;
    }

    /**
     * @param connectionType
     * @return string
     */
    public static String connectionTypeChecker(int connectionType) {
        String type = null;
        switch (connectionType) {
            case TYPE_WIFI:
                type = "TYPE_WIFI";
                break;
            case TYPE_MOBILE:
                type = "TYPE_MOBILE";
                break;
        }
        return type;
    }

}
