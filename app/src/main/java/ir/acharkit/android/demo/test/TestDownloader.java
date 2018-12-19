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
import ir.acharkit.android.util.Log;
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
                getDownloader().pauseDownload();
            }
        });

        findViewById(R.id.resume_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkAvailable(getActivity())) {
                    getDownloader().resumeDownload();
                }
            }
        });

    }

    public boolean checkNetworkAvailable(Context context) {
        return ConnectChecker.isInternetAvailable(context);
    }

    private Downloader.Builder getDownloader() {
        Map<String, String> header = new HashMap<>();
        header.put("Access-Token", "1234567890ABC");
        return new Downloader.Builder(getApplicationContext(), "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg")
                .setDownloadDir(String.valueOf(getExternalFilesDir("download")))
                .setTimeOut(60 * 2000)
                .setFileName("image", "jpeg")
                .setHeader(header)
                .setDownloadListener(new OnDownloadListener() {
                    @Override
                    public void onCompleted(File file) {
                        Log.d(TAG, "onCompleted:");
                        Util.showToast(getApplicationContext(), "onCompleted", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(String reason) {
                        Log.d(TAG, "onFailure:" + reason);
                        Util.showToast(getApplicationContext(), "onFailure:" + reason, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void progressUpdate(int percent, int downloadedSize, int totalSize) {
                        Log.d(TAG, "progressUpdate:" + percent);
                    }
                });
    }
}
