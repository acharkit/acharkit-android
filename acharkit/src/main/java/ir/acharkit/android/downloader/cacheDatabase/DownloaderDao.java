package ir.acharkit.android.downloader.cacheDatabase;

import android.content.Context;
import android.database.Cursor;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/3/18
 * Email:   alirezat775@gmail.com
 */

public class DownloaderDao {

    private final DownloaderDatabase database;

    public DownloaderDao(Context context) {
        database = DownloaderDatabase.getInstance(context);
    }

    public void insertNewDownload(DownloaderModel downloaderModel) {
        String query = "insert into " + DownloaderModel.TABLE_NAME + " " +
                "(" +
                DownloaderModel.Column.URL + "," +
                DownloaderModel.Column.FILE_NAME + "," +
                DownloaderModel.Column.STATUS + "," +
                DownloaderModel.Column.PERCENT + "," +
                DownloaderModel.Column.SIZE + "," +
                DownloaderModel.Column.TOTAL_SIZE
                + ")" +
                " VALUES(" + "\"" + downloaderModel.getUrl() + "\"" + ","
                + " " + "\"" + downloaderModel.getFileName() + "\"" + ","
                + " " + downloaderModel.getStatus() + ","
                + " " + downloaderModel.getPercent() + ","
                + " " + downloaderModel.getSize() + ","
                + " " + downloaderModel.getTotalSize() + ")";
        database.getWritableDatabase().execSQL(query);
        database.close();
    }

    public void updateDownload(String url, int status, int percent, int size, int totalSize) {
        String query = "update " + DownloaderModel.TABLE_NAME + " " +
                " set " + DownloaderModel.Column.STATUS + " = " + status +
                " , " + DownloaderModel.Column.PERCENT + " = " + percent +
                " , " + DownloaderModel.Column.SIZE + " = " + size +
                " , " + DownloaderModel.Column.TOTAL_SIZE + " = " + totalSize +
                " where " + DownloaderModel.Column.URL + " = " + "\"" + url + "\"";
        database.getWritableDatabase().execSQL(query);
        database.close();
    }

    public DownloaderModel getDownload(String url) {
        DownloaderModel model = null;
        String query = "select * from " + DownloaderModel.TABLE_NAME + " where " + DownloaderModel.Column.URL + " = \"" + url + "\"";
        Cursor cursor = database.getReadableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(cursor.getColumnIndex(DownloaderModel.Column.ID));
            String itemUrl = cursor.getString(cursor.getColumnIndex(DownloaderModel.Column.URL));
            String itemFileName = cursor.getString(cursor.getColumnIndex(DownloaderModel.Column.FILE_NAME));
            int itemStatus = cursor.getInt(cursor.getColumnIndex(DownloaderModel.Column.STATUS));
            int itemPercent = cursor.getInt(cursor.getColumnIndex(DownloaderModel.Column.PERCENT));
            int itemSize = cursor.getInt(cursor.getColumnIndex(DownloaderModel.Column.SIZE));
            int itemTotalSize = cursor.getInt(cursor.getColumnIndex(DownloaderModel.Column.TOTAL_SIZE));
            model = new DownloaderModel(itemId, itemUrl, itemFileName, itemStatus, itemPercent, itemSize, itemTotalSize);
        }
        cursor.close();
        database.close();
        return model;
    }
}
