package ir.acharkit.android.util;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.PermissionChecker;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:      Alireza Mahmoodi
 * Created:     5/24/2017
 * Email:       mahmoodi.dev@gmail.com
 * Website:     alirezamh.com
 */

public class PermissionRequest {

    private static Map<Integer, Request> requests = new HashMap<>();
    private final Request request = new Request();

    /**
     * @param fragment
     * @param permissions
     */
    public PermissionRequest(@NonNull Fragment fragment, @NonNull String... permissions) {
        request.permissions = permissions;
        request.fragment = fragment;
        request.code = (int) Math.round(Math.random() * 10000);
    }

    /**
     * @param activity
     * @param permissions
     */
    public PermissionRequest(@NonNull AppCompatActivity activity, @NonNull String... permissions) {
        request.permissions = permissions;
        request.activity = activity;
        request.code = (int) Math.round(Math.random() * 10000);
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(@NonNull int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requests.containsKey(requestCode)) {
            if (isGrantedRequests(grantResults)) {
                requests.get(requestCode).granted();
            } else {
                if (requests.get(requestCode).repeat > 0) {
                    requests.get(requestCode).resend();
                } else {
                    requests.get(requestCode).notGranted();
                }
            }
        }
    }

    /**
     * @param grantResults
     * @return
     */
    private static boolean isGrantedRequests(@NonNull int[] grantResults) {
        for (int g : grantResults) {
            if (g != PermissionChecker.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    /**
     * @return
     */
    public boolean isGranted() {
        for (String p : request.permissions) {
            if (ActivityCompat.checkSelfPermission(request.getContext(), p) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    /**
     *
     */
    public void send() {
        send(1);
    }

    /**
     * @param repeatCount
     */
    public void send(@NonNull int repeatCount) {
        request.send(repeatCount - 1);
    }

    /**
     * @param onGrant
     */
    public void setOnGrantListener(@NonNull Runnable onGrant) {
        request.onGrant = onGrant;
    }

    /**
     * @param onNotGrant
     */
    public void setOnNotGrantListener(@NonNull Runnable onNotGrant) {
        request.onNotGrant = onNotGrant;
    }

    /**
     *
     */
    private static class Request {
        public int code;
        private AppCompatActivity activity;
        private Fragment fragment;
        private String[] permissions;
        private Runnable onGrant;
        private Runnable onNotGrant;
        private int repeat;

        /**
         * @param repeatCount
         */
        public void send(int repeatCount) {
            List<String> notGranted = null;
            for (String p : permissions) {
                if (ActivityCompat.checkSelfPermission(getContext(), p) != PackageManager.PERMISSION_GRANTED) {
                    if (notGranted == null) notGranted = new ArrayList<>();
                    notGranted.add(p);
                }
            }
            if (notGranted == null) return;

            repeat = repeatCount;
            if (!requests.containsKey(code)) requests.put(code, this);
            if (fragment == null) {
                ActivityCompat.requestPermissions(activity, notGranted.toArray(new String[0]), code);
            } else {
                fragment.requestPermissions(notGranted.toArray(new String[0]), code);
            }
        }

        /**
         *
         */
        public void resend() {
            send(repeat - 1);
        }

        /**
         *
         */
        public void granted() {
            if (!requests.containsKey(code)) requests.remove(code);
            if (onGrant != null) {
                onGrant.run();
            }
        }

        /**
         *
         */
        public void notGranted() {
            if (!requests.containsKey(code)) requests.remove(code);
            if (onNotGrant != null) {
                onNotGrant.run();
            }
        }

        /**
         * @return
         */
        public Context getContext() {
            if (activity != null) return activity;
            return fragment.getContext();
        }
    }

}