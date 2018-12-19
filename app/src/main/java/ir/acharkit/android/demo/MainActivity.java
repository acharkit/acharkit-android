package ir.acharkit.android.demo;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import ir.acharkit.android.date.DateUtil;
import ir.acharkit.android.demo.test.TestBottomSheet;
import ir.acharkit.android.demo.test.TestBottomTab;
import ir.acharkit.android.demo.test.TestCarouselPager;
import ir.acharkit.android.demo.test.TestDialog;
import ir.acharkit.android.demo.test.TestDownloader;
import ir.acharkit.android.demo.test.TestFragment;
import ir.acharkit.android.demo.test.TestGif;
import ir.acharkit.android.demo.test.TestImageLoader;
import ir.acharkit.android.demo.test.TestIntroPager;
import ir.acharkit.android.demo.test.TestProgress;
import ir.acharkit.android.demo.test.TestTag;
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
        calendar();


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

        findViewById(R.id.start_activity_bottom_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestBottomTab.class);
            }
        });

        findViewById(R.id.start_activity_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestBottomSheet.class);
            }
        });

        findViewById(R.id.start_activity_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestTag.class);
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
                startActivity(TestDownloader.class);
            }
        });
    }

    private void calendar() {
        Log.i(TAG, "getYear : " + DateUtil.getPersianDate().getYear());
        Log.i(TAG, "getMonth : " + DateUtil.getPersianDate().getMonth());
        Log.i(TAG, "getDayOfMonth : " + DateUtil.getPersianDate().getDayOfMonth());
        Log.i(TAG, "isLeapYear : " + DateUtil.getPersianDate().isLeapYear());

        Log.i(TAG, "getYear : " + DateUtil.getIslamicDate().getYear());
        Log.i(TAG, "getMonth : " + DateUtil.getIslamicDate().getMonth());
        Log.i(TAG, "getDayOfMonth : " + DateUtil.getIslamicDate().getDayOfMonth());

        Log.i(TAG, "getYear : " + DateUtil.getCivilDate().getYear());
        Log.i(TAG, "getMonth : " + DateUtil.getCivilDate().getMonth());
        Log.i(TAG, "getDayOfMonth : " + DateUtil.getCivilDate().getDayOfMonth());

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
        Database.getInstance().prepareDatabaseFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), "db.sqlite", 1);
        Database.getInstance().prepareDatabaseAssets("db.sqlite", 1);
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
            parameter.put("title", "foo");
            parameter.put("body", "bar");
            parameter.put("userId", 1);
        } catch (JSONException e) {
            Log.w(TAG, e);
        }

        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json");

        if (checkNetworkAvailable(this)) {
            new ConnectionRequest.Builder(this, ConnectionRequest.Method.POST, "https://jsonplaceholder.typicode.com/posts")
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
