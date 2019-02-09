package ir.acharkit.android.downloader.cacheDatabase;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/3/18
 * Email:   alirezat775@gmail.com
 */

public class DownloaderModel {

    static final String TABLE_NAME = "downloader";

    public static class Status {
        public static final int NEW = 1;
        public static final int DOWNLOADING = 2;
        public static final int SUCCESS = 3;
        public static final int FAIL = 4;
        public static final int PAUSE = 5;
    }

    static class Column {
        static final String ID = "id";
        static final String URL = "url";
        static final String FILE_NAME = "file_name";
        static final String STATUS = "status";
        static final String PERCENT = "percent";
        static final String SIZE = "size";
        static final String TOTAL_SIZE = "total_size";
    }

    static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + Column.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Column.URL + " TEXT,"
                    + Column.FILE_NAME + " TEXT,"
                    + Column.STATUS + " INTEGER,"
                    + Column.PERCENT + " INTEGER,"
                    + Column.SIZE + " INTEGER,"
                    + Column.TOTAL_SIZE + " INTEGER"
                    + ")";

    private int id;
    private String url;
    private String fileName;
    private int status;
    private int percent;
    private int size;
    private int totalSize;

    public DownloaderModel(int id, String url, String fileName, int status, int percent, int size, int totalSize) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.status = status;
        this.percent = percent;
        this.size = size;
        this.totalSize = totalSize;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public int getStatus() {
        return status;
    }

    public int getPercent() {
        return percent;
    }

    public int getSize() {
        return size;
    }

    public int getTotalSize() {
        return totalSize;
    }
}
