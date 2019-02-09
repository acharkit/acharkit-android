package ir.acharkit.android.downloader.cacheDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ir.acharkit.android.util.Logger;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/3/18
 * Email:   alirezat775@gmail.com
 */

class DownloaderDatabase extends SQLiteOpenHelper {

    private static final String TAG = DownloaderDatabase.class.getName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "downloader_db";
    private static DownloaderDatabase instance;

    public static DownloaderDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new DownloaderDatabase(context);
        }
        return instance;
    }

    private DownloaderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Logger.d(TAG, "onCreate SQLiteDatabase!!!");
        sqLiteDatabase.execSQL(DownloaderModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
