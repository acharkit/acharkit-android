# AcharKit

  AcharKit Framework
  
  
  For more information, go to the site of [AcharKit](http://www.acharkit.ir)

```code
    compile project(':acharkit-release-0.0.2')
    or
    implementation project(':acharkit-release-0.0.2')
```

# [download verson release-0.0.2](https://raw.githubusercontent.com/acharkit/acharkit-android/master/release/acharkit-release-0.0.2.aar)


Note : Helper classes have many methods that may not be used here

<br/>

Use AbstractActivity
```code
   yourActivity extends AbstractActivity
```

Use AbstractFragment
```code
   yourFragment extends AbstractFragment
```

Use your fragment
```code
   useFragment = new UseFragment();
                   useFragment.setTagId("tagId");
                   useFragment.actionFragment(R.id.frameLayout, AbstractFragment.TYPE_REPLACE / AbstractFragment.TYPE_ADD, addToBackStack);
```

Remove fragment from backStack
```code
    useFragment.removeFragmentPopBackStack();
```

Use AbstractTabLayout
```code
        final AbstractTabLayout tab = new AbstractTabLayout(activity, R.id.viewPager, R.id.tabLayout);

        tab.setOffscreenPageLimit(3);                   // optional
        tab.setIndicatorColor(0xffffdd33);              // optional
        tab.setIndicatorHeight(4);                      // optional
        tab.setTabItemColor(0xffff0033, 0xff00ee11);    // optional

        tab.add(useFragment0.setTagId(0), "One", R.mipmap.ic_launcher);
        tab.add(useFragment1.setTagId(1), "Two", R.mipmap.ic_launcher);
        tab.add(useFragment2.setTagId(2), "Three", R.mipmap.ic_launcher);
```

Use AbstractDialog
```code

    builder = new AbstractDialog.Builder(TestDialog.this);
                    builder.setBackgroundColor(0xFF232323, 8)
                            .setBackgroundResource(R.mipmap.ic_launcher)
                            .setSize(0.8f, 0.8f)
                            .setFont("OpenSans.ttf", Typeface.NORMAL)
                            .setTitle("title", 5, 0xFFFFFFFF)
                            .setMessage("message", 5, 0xFFFFFFFF)
                            .setButtonsViewOrientation(LinearLayout.HORIZONTAL)
                            .addButton("button1", 5, 0xFF0A8A12, 0xFFFFFFFF, onClicklistenerOne(), Gravity.CENTER, 8)
                            .addDismissButton("dismiss", 5, 0xFFFF0000, 0xFFFFFFFF, Gravity.CENTER, 8)
                            .setCancelable(true)
                            .setCanceledOnTouchOutside(false)
                            .setOnCancelListener(onCancelListener())
                            .setOnDismissListener(onDismissListener())
                            .show();
```

Use Log
```code
   Log.setDebugMode(true);
   Log.setAcharkitLog(true);

   Log.i("tag","info");
   Log.d("tag","debug");
   Log.w("tag",throwable);
   Log.e("tag",throwable,"error");
```

Use Permission Request
```code
   PermissionRequest permissionRequest = new PermissionRequest(fragment / activity, Manifest.permission.EXAMPLE);
   permissionRequest.send();
```

Use Cache
```code
    Cache.setContext(context);
    Cache.put("test_key", "test value");
    Cache.get("test_key", "");
```

Use Crypt
```code
    Crypt crypt = new Crypt("yourKeyMustBeHave24Character");
            String encrypted = crypt.encrypt("data");
            String decrypted = crypt.decrypt(encrypted);
```

Use Connection Request
```code
    ConnectionRequest.Builder builder = new ConnectionRequest.Builder(this, ConnectionRequest.Method.POST, "https://example.com/")
                        .setHeader(header)
                        .trustSSL(true)
                        .setParameters(jsonObject)
                        .setOnRequestListener(new OnRequestListener() {
                            @Override
                            public void success(String response) {
                                Log.d(TAG, "response:" + response);
                            }
                            @Override
                            public void error(String error) {
                                Log.d(TAG, "error:" + error);
                            }
    
                        });
                builder.sendRequest();
```

Use Downloader
```code
    Downloader.Builder builder = new Downloader.Builder(getApplicationContext(), "http://www.xsjjys.com/data/out/60/WHDQ-512049955.png")
                        .setDownloadDir(String.valueOf(getExternalFilesDir("download")))
                        .setTimeOut(60 * 1000)
                        .setFileName("image","png")
                        .trustSSL(true)
                        .setHeader(header)
                        .setDownloadListener(new OnDownloadListener() {
                            @Override
                            public void onCompleted() {
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
                        });
                builder.download();
```

Use ImageLoader
```code

        ImageLoader.Builder builder = new ImageLoader.Builder(context, imageView)
        .setPlaceHolder(R.mipmap.ic_launcher)
        .setTrust(true)
        .setTimeOut(60 * 1000)
        .setOnImageLoadListener(new OnImageLoadListener() {
            @Override
            public void onCompleted(ImageView imageView) {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onFailure(String reason) {
                Log.d(TAG, "fail: " + reason);
            }
        });
        builder.load("http://www.xsjjys.com/data/out/60/WHDQ-512049955.png");


```

Use Internet Connection
```code

    public boolean checkNetworkAvailable(Context context) {
        return ConnectChecker.isInternetAvailable(context);
    }

    public int connectionType(Context context) {
        return ConnectChecker.connectionType(context);
    }

    public String connectionTypeChecker(int connectionType) {
        return ConnectChecker.connectionTypeChecker(connectionType);
    }
```

Use Font
```code
        Font.fromAsset(context, "font.ttf", Typeface.NORMAL, view);
        Font.setFontViewGroup(viewGroup, context, "font.ttf", Typeface.NORMAL);
```

Use Indicator Pager
```code

    <ir.acharkit.android.component.indicator.ViewPagerIndicator
        android:id="@+id/indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        ...
        ...
        ...
        app:delimiterSize="8dp"
        app:itemIcon="@mipmap/ic_launcher"
        app:itemSize="8dp"/>
        
        
        final IndicatorPager indicatorPager = new IndicatorPager(this, R.id.viewPager, R.id.indicator);
        indicatorPager.setOffscreenPageLimit(3);
        indicatorPager.add(useFragment0.setTagId("0"));
```

Use Card List
```code

    <ir.acharkit.android.component.cardList.CardListView
        android:id="@+id/carousel"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

        TestCardListAdapter carouselPagerAdapter = new TestCardListAdapter(this);
        final CardList cardList = new CardList(this, R.id.carousel, carouselPagerAdapter);
        cardList.setOrientation(CardListView.HORIZONTAL, true);
        cardList.setAutoScroll(true, 5000, true);
        cardList.setScaleView(true);
        
        for(int i = 0; i < 10; i++) {
        TestCardListModel model = new TestCardListModel();
        ...
        ...
        ...
        cardList.add(model);
        }

```

Use ConvertHelper
```code
        ConvertHelper.dpToPx(context, 5);
        ConvertHelper.pixelsToDp(context, 5);
        ConvertHelper.pxToSp(context, 5);
        ConvertHelper.spToPx(context, 5);
        ConvertHelper.stringToByte("data");
        ConvertHelper.byteToString(new byte[16]);
```

Use DatabaseHelper
```code
        Database.init(this);
        Database.getInstance().prepareDB("db.sqlite", 1);
```

Use DateTimeHelper
```code
        DateTimeHelper.currentDateTime("UTC");
        DateTimeHelper.millisToStringDate(1509663266000L);
        DateTimeHelper.dateStringToMillis("2017/11/02 22:54:26");
```

Use StringHelper
```code
        StringHelper.capitalizeString("this is test");
        StringHelper.isEmpty("this is test");
        StringHelper.isEmpty("");
        StringHelper.isNotEmpty("this is test");
        StringHelper.isNotEmpty("");
        StringHelper.upperCaseFirst("this is test");
        StringHelper.lowerCaseFirst("This is test");
        StringHelper.SHA1("this is test");
```

Use ViewHelper
```code
        ViewHelper.setMargins(context, view, left, top, right, bottom);
        ViewHelper.setFullScreen(activity);
        Arrays.toString(ViewHelper.getScreenSize(activity));
        ViewHelper.getScreenSize(activity);
        ViewHelper.getScreenHeight(activity);
        ViewHelper.getScreenWidth(activity);
        ViewHelper.getSleepDuration(activity);
        ViewHelper.isScreenLock(activity);
        ViewHelper.isPortrait(activity);
        ViewHelper.isLandscape(activity);
        ViewHelper.isTablet(activity);
```

Use MimeHelper
```code
        MimeHelper.getFileExtensionFromUrl("image.test.jpg");
        MimeHelper.hasExtension("jpg");
        MimeHelper.hasMimeType("audio/aac");
        MimeHelper.guessMimeTypeFromExtension(MimeHelper.getFileExtensionFromUrl("image.test.jpg"));
```

Use Util
```code
        Util.copyToClipboard(activity, "test", "clipboard copyToClipboard");
        Util.isValidateEmail("test@example.com");
        Util.arabicToDecimal("٠١٢٣٤٥٦٧٨٩");
        Util.isValidPhoneNumberIran("09101234567");

        Util.showToast(context, "show toast test", Toast.LENGTH_SHORT, backgroundColor, messageColor, Typeface.ITALIC, "OpenSans.ttf");
        Util.showToast(context, "show toast test", Toast.LENGTH_SHORT, Typeface.ITALIC, "OpenSans.ttf");
        Util.showToast(context, "show toast test", Toast.LENGTH_SHORT);
```
