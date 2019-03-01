package ir.acharkit.android.demo.test;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private Downloader downloader;
    private Button startDownload;
    private Button stopDownload;
    private Button pauseDownload;
    private Button resumeDownload;
    private ProgressBar percentProgress;
    private TextView percentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_donwloader);

        startDownload = findViewById(R.id.start_download);
        stopDownload = findViewById(R.id.stop_download);
        pauseDownload = findViewById(R.id.pause_download);
        resumeDownload = findViewById(R.id.resume_download);
        percentTxt = findViewById(R.id.percent_txt);
        percentProgress = findViewById(R.id.percent_progress);

        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkAvailable(getActivity())) {
                    enableDownload();
                    buildDownloader();
                    downloader.download();
                }
            }
        });

        stopDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableDownload();
                if (downloader != null) downloader.cancelDownload();
            }
        });

        pauseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableDownload();
                if (downloader != null) downloader.pauseDownload();
            }
        });

        resumeDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkAvailable(getActivity())) {
                    buildDownloader();
                    downloader.resumeDownload();
                    enableDownload();
                }
            }
        });
    }

    public boolean checkNetworkAvailable(Context context) {
        return ConnectChecker.isInternetAvailable(context);
    }

    private void enableDownload() {
        startDownload.setEnabled(false);
        resumeDownload.setEnabled(false);
        stopDownload.setEnabled(true);
        pauseDownload.setEnabled(true);
    }

    private void disableDownload() {
        startDownload.setEnabled(true);
        resumeDownload.setEnabled(true);
        stopDownload.setEnabled(false);
        pauseDownload.setEnabled(false);
    }

    private void buildDownloader() {
        Map<String, String> header = new HashMap<>();
        header.put("Access-Token", "1234567890ABC");
        downloader = new Downloader.Builder(getApplicationContext(), "https://dl3.android30t.com/apps/Q-U/Instagram-v79.0.0.21.101(Android30t.Com).apk")
                .setDownloadDir(String.valueOf(getExternalFilesDir("download")))
                .setTimeOut(60 * 2000)
                .setFileName("test_application", "apk")
                .setHeader(header)
                .setDownloadListener(new OnDownloadListener() {
                    @Override
                    public void onCompleted(File file) {
                        Logger.d(TAG, "onCompleted:");
                        Util.showToast(getApplicationContext(), "onCompleted", Toast.LENGTH_LONG);
                        enableDownload();
                    }

                    @Override
                    public void onFailure(String reason) {
                        Logger.d(TAG, "onFailure:" + reason);
                        Util.showToast(getApplicationContext(), "onFailure:" + reason, Toast.LENGTH_LONG);
                        enableDownload();
                    }

                    @Override
                    public void progressUpdate(int percent, int downloadedSize, int totalSize) {
                        Logger.d(TAG, "progressUpdate:" + percent);
                        updateUi(percent);
                    }

                    @Override
                    public void onCancel() {
                        Logger.d(TAG, "onCancel:" + "canceled");
                        Util.showToast(getApplicationContext(), "onCancel:" + "canceled", Toast.LENGTH_LONG);
                        enableDownload();
                    }
                }).build();
    }

    private void updateUi(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                percentTxt.setText(percent + " %");
                percentProgress.setProgress(percent);
            }
        });
    }
}
