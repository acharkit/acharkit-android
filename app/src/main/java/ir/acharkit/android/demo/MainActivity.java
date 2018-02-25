package ir.acharkit.android.demo;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.connection.ConnectionRequest;
import ir.acharkit.android.connection.OnRequestListener;
import ir.acharkit.android.demo.test.TestCarouselPager;
import ir.acharkit.android.demo.test.TestDialog;
import ir.acharkit.android.demo.test.TestFragment;
import ir.acharkit.android.demo.test.TestGif;
import ir.acharkit.android.demo.test.TestImageLoader;
import ir.acharkit.android.demo.test.TestIntroPager;
import ir.acharkit.android.demo.test.TestProgress;
import ir.acharkit.android.demo.test.TestUtils;
import ir.acharkit.android.demo.test.TestViewPager;
import ir.acharkit.android.downloader.Downloader;
import ir.acharkit.android.downloader.OnDownloadListener;
import ir.acharkit.android.util.Cache;
import ir.acharkit.android.util.ConnectChecker;
import ir.acharkit.android.util.Crypt;
import ir.acharkit.android.util.Database;
import ir.acharkit.android.util.Font;
import ir.acharkit.android.util.Log;
import ir.acharkit.android.util.PermissionRequest;
import ir.acharkit.android.util.Util;

public class MainActivity extends AbstractActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log();
        permissionRequest();
        cache();
        initDatabase();
        crypt();
        font();

        findViewById(R.id.start_activity_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestFragment.class);
            }
        });

        findViewById(R.id.start_activity_viewpager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestViewPager.class);
            }
        });

        findViewById(R.id.start_activity_carousel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestCarouselPager.class);
            }
        });

        findViewById(R.id.start_activity_intro_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestIntroPager.class);
            }
        });

        findViewById(R.id.start_activity_utils).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestUtils.class);
            }
        });

        findViewById(R.id.start_activity_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestGif.class);
            }
        });

        findViewById(R.id.start_activity_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestProgress.class);
            }
        });

        findViewById(R.id.start_activity_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestDialog.class);
            }
        });

        findViewById(R.id.start_activity_image_loader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestImageLoader.class);
            }
        });

        findViewById(R.id.start_request_connection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestConnection();
            }
        });

        findViewById(R.id.start_request_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDownload();
            }
        });
    }

    private void font() {
        Font.fromAsset(this, "OpenSans.ttf", Typeface.BOLD_ITALIC, findViewById(R.id.start_activity_fragment));
        Font.fromAsset(this, "OpenSans.ttf", Typeface.BOLD_ITALIC, findViewById(R.id.start_activity_viewpager));
        View view = this.getWindow().getDecorView();
        Font.setFontViewGroup((ViewGroup) view, this, "OpenSans.ttf", Typeface.BOLD);
    }

    private void crypt() {
        Crypt crypt = new Crypt("ThisIsSpartaThisIsSparta");
        String encrypted = crypt.encrypt("data");
        String decrypted = crypt.decrypt(encrypted);
    }

    private void initDatabase() {
        Database.init(this);
        Database.getInstance().prepareDB("db.sqlite", 1);
    }

    private void cache() {
        Cache.setContext(this);
        Cache.put("test_key", "test value");
        Cache.get("test_key", "");
    }

    private void permissionRequest() {
        PermissionRequest permissionRequest = new PermissionRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionRequest.send();
    }

    private void log() {
        Log.setDebugMode(true);
        Log.setAcharkitLog(true);
    }

    private void requestConnection() {
        JSONObject parameter = new JSONObject();
        try {
            parameter.put("test1", 1);
            parameter.put("test2", "value");
        } catch (JSONException e) {
            Log.w(TAG, e);
        }

        Map<String, String> header = new HashMap<>();
        header.put("Access-Token", "1234567890ABC");

        if (checkNetworkAvailable(this)) {
            new ConnectionRequest.Builder(this, ConnectionRequest.Method.POST, "http://www.test.com/api")
                    .trustSSL(true)
                    .setHeader(header)
                    .setParameters(parameter)
                    .setTimeOut(60 * 2000)
                    .setOnRequestListener(new OnRequestListener() {
                        @Override
                        public void error(String error) {
                            Log.d(TAG, "error:" + error);
                            Util.showToast(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void success(String response) {
                            Log.d(TAG, "response:" + response);
                            Util.showToast(getApplicationContext(), response, Toast.LENGTH_SHORT);
                        }
                    }).sendRequest();
        }
    }

    private void requestDownload() {
        Map<String, String> header = new HashMap<>();
        header.put("Access-Token", "1234567890ABC");

        if (checkNetworkAvailable(this)) {
            new Downloader.Builder(getApplicationContext(), "http://www.xsjjys.com/data/out/60/WHDQ-512049955.png")
                    .setDownloadDir(String.valueOf(getExternalFilesDir("download")))
                    .setTimeOut(60 * 2000)
                    .setFileName("image", "png")
                    .trustSSL(true)
                    .setHeader(header)
                    .setDownloadListener(new OnDownloadListener() {
                        @Override
                        public void onCompleted(File file) {
                            Log.d(TAG, "onCompleted:");
                        }

                        @Override
                        public void onFailure(String reason) {
                            Log.d(TAG, "onFailure:" + reason);
                        }

                        @Override
                        public void progressUpdate(int percent, int downloadedSize, int totalSize) {
                            Log.d(TAG, "progressUpdate:" + percent);
                        }
                    }).download();

        }
    }

    public boolean checkNetworkAvailable(Context context) {
        return ConnectChecker.isInternetAvailable(context);
    }

    public int connectionType(Context context) {
        return ConnectChecker.connectionType(context);
    }

    public String connectionTypeChecker(int connectionType) {
        return ConnectChecker.connectionTypeChecker(connectionType);
    }

}
