package ir.acharkit.android.demo.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.util.Font;
import ir.acharkit.android.util.Logger;
import ir.acharkit.android.util.PermissionRequest;
import ir.acharkit.android.util.Util;
import ir.acharkit.android.util.helper.ConvertHelper;
import ir.acharkit.android.util.helper.DateTimeHelper;
import ir.acharkit.android.util.helper.IntentHelper;
import ir.acharkit.android.util.helper.MimeHelper;
import ir.acharkit.android.util.helper.StringHelper;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/26/2017
 * Email:   alirezat775@gmail.com
 */
public class TestUtils extends AbstractActivity {

    private static final String TAG = TestUtils.class.getSimpleName();
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private LinearLayout linearLayout;
    private Button smsIntent;
    private Button callIntent;
    private Button browserIntent;
    private Button emailIntent;
    private Button toastTest;

    private void initView() {
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        linearLayout.setLayoutParams(layoutParams);

        smsIntent = new Button(this);
        smsIntent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        smsIntent.setText("smsIntent");
        ViewHelper.setMargins(this, smsIntent, 10, 10, 10, 10);

        callIntent = new Button(this);
        callIntent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        callIntent.setText("callIntent");
        ViewHelper.setMargins(this, callIntent, 10, 10, 10, 10);

        browserIntent = new Button(this);
        browserIntent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        browserIntent.setText("browserIntent");
        ViewHelper.setMargins(this, browserIntent, 10, 10, 10, 10);

        emailIntent = new Button(this);
        emailIntent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        emailIntent.setText("emailIntent");
        ViewHelper.setMargins(this, emailIntent, 10, 10, 10, 10);

        toastTest = new Button(this);
        toastTest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        toastTest.setText("toastTest");
        ViewHelper.setMargins(this, toastTest, 10, 10, 10, 10);

        linearLayout.addView(smsIntent);
        linearLayout.addView(callIntent);
        linearLayout.addView(browserIntent);
        linearLayout.addView(emailIntent);
        linearLayout.addView(toastTest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setContentView(linearLayout);
        smsIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionRequest permissionRequest = new PermissionRequest(TestUtils.this, Manifest.permission.SEND_SMS);
                permissionRequest.send();
                if (ActivityCompat.checkSelfPermission(TestUtils.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                IntentHelper.smsIntent(TestUtils.this, 5554, "test");
            }
        });

        callIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionRequest permissionRequest = new PermissionRequest(TestUtils.this, Manifest.permission.CALL_PHONE);
                permissionRequest.send();
                if (permissionRequest.isGranted()) {
                    if (ActivityCompat.checkSelfPermission(TestUtils.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    IntentHelper.callIntent(TestUtils.this, 5554);
                }
            }
        });

        browserIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.browserIntent(TestUtils.this, "https://www.google.com/");
            }
        });

        emailIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.emailIntent(TestUtils.this, "tesr@example.com", "TEST", "test");
            }
        });


        toastTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showToast(TestUtils.this, "show toast test", Toast.LENGTH_SHORT, 0xff00ffdd, 0xff000000, "OpenSans.ttf", Typeface.ITALIC);
                Util.showToast(TestUtils.this, "show toast test", Toast.LENGTH_SHORT, "OpenSans.ttf", Typeface.ITALIC);
                Util.showToast(TestUtils.this, "show toast test", Toast.LENGTH_SHORT);
            }
        });

        Font.setFontViewGroup(linearLayout, this, "OpenSans.ttf", Typeface.BOLD);

        testConvertHelper();
        testStringHelper();
        testDateTimeHelper();
        testMimeHelper();
        testUtil();
        testViewHelper();

    }

    private void testViewHelper() {
        Logger.i(TAG, "ViewHelper" + "\n" +
                Arrays.toString(ViewHelper.getScreenSize(this)) + "\n" +
                ViewHelper.getScreenHeight() + "\n" +
                ViewHelper.getScreenWidth() + "\n" +
                ViewHelper.getSleepDuration(this) + "\n" +
                ViewHelper.isScreenLock(this) + "\n" +
                ViewHelper.isPortrait(this) + "\n" +
                ViewHelper.isLandscape(this) + "\n" +
                ViewHelper.isTablet(this));

    }

    private void testDateTimeHelper() {
        Logger.i(TAG, "DateTimeHelper" + "\n" +
                DateTimeHelper.currentDateTime("UTC") + "\n" +
                DateTimeHelper.millisToStringDate(1509663266000L) + "\n" +
                DateTimeHelper.dateStringToMillis("2017/11/02 22:54:26"));

    }

    private void testMimeHelper() {
        Logger.i(TAG, "MimeHelper" + "\n" +
                MimeHelper.getFileExtensionFromUrl("image.fromyesterday.test.jpg") + "\n" +
                MimeHelper.hasExtension("jpg") + "\n" +
                MimeHelper.hasMimeType("audio/aac") + "\n" +
                MimeHelper.guessMimeTypeFromExtension(MimeHelper.getFileExtensionFromUrl("image.fromyesterday.test.jpg")));

    }

    private void testUtil() {
        Logger.i(TAG, "UtilsConnection" + "\n" +
                Util.isValidateEmail("test@example.com") + "\n" +
                Util.arabicToDecimal("٠١٢٣٤٥٦٧٨٩") + "\n" +
                Util.isValidPhoneNumberIran("09101234567"));
        Util.copyToClipboard(TestUtils.this, "test", "clipboard copyToClipboard");
        Util.copyFromAssets(this, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), "db.sqlite.zip");
    }

    private void testConvertHelper() {
        Logger.i(TAG, "ConvertHelper" + "\n" +
                ConvertHelper.dpToPx(this, 5) + "\n" +
                ConvertHelper.pixelsToDp(this, 5) + "\n" +
                ConvertHelper.pxToSp(this, 5) + "\n" +
                ConvertHelper.spToPx(this, 5) + "\n" +
                ConvertHelper.stringToByte("data") + "\n" +
                ConvertHelper.byteToString(new byte[16]));

    }

    private void testStringHelper() {
        Logger.i(TAG, "StringHelper" + "\n" +
                StringHelper.capitalizeString("this is test") + "\n" +
                StringHelper.isEmpty("this is test") + "\n" +
                StringHelper.isEmpty("") + "\n" +
                StringHelper.isNotEmpty("this is test") + "\n" +
                StringHelper.isNotEmpty("") + "\n" +
                StringHelper.upperCaseFirst("this is test") + "\n" +
                StringHelper.lowerCaseFirst("This is test") + "\n" +
                StringHelper.SHA1("this is test"));
    }
}
