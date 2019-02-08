package ir.acharkit.android.demo.test;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.downloader.Downloader;
import ir.acharkit.android.downloader.OnDownloadListener;
import ir.acharkit.android.util.ConnectChecker;
import ir.acharkit.android.util.Logger;
import ir.acharkit.android.util.Util;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class TestDownloader extends AbstractActivity {

    public static final String TAG = TestDownloader.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_donwloader);

        findViewById(R.id.start_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkAvailable(getActivity())) {
                    getDownloader().download();
                }
            }
        });

        findViewById(R.id.stop_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDownloader().cancelDownload();
            }
        });

        findViewById(R.id.pause_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getDownloader().pauseDownload();
            }
        });

        findViewById(R.id.resume_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkAvailable(getActivity())) {
//                    getDownloader().resumeDownload();
                }
            }
        });
    }

    public boolean checkNetworkAvailable(Context context) {
        return ConnectChecker.isInternetAvailable(context);
    }

    private Downloader getDownloader() {
        Map<String, String> header = new HashMap<>();
        header.put("Access-Token", "1234567890ABC");
        Downloader.Builder builder = new Downloader.Builder(getApplicationContext(), "https://static.approo.ir/apk/com.vada.hafezproject-v100081.apk")
                .setDownloadDir(String.valueOf(getExternalFilesDir("download")))
                .setTimeOut(60 * 2000)
                .setFileName("test_app", "apk")
                .setHeader(header)
                .setDownloadListener(new OnDownloadListener() {
                    @Override
                    public void onCompleted(File file) {
                        Logger.d(TAG, "onCompleted:");
                        Util.showToast(getApplicationContext(), "onCompleted", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(String reason) {
                        Logger.d(TAG, "onFailure:" + reason);
                        Util.showToast(getApplicationContext(), "onFailure:" + reason, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void progressUpdate(int percent, int downloadedSize, int totalSize) {
                        Logger.d(TAG, "progressUpdate:" + percent);
                    }

                    @Override
                    public void onCancel() {
                        Logger.d(TAG, "onCancel:" + "canceled");
                        Util.showToast(getApplicationContext(), "onCancel:" + "canceled", Toast.LENGTH_LONG);
                    }
                });

        return builder.build();
    }
}
