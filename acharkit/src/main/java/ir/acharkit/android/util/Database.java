package ir.acharkit.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Database {

    private static final String TAG = Database.class.getName();
    private static Database mInstance;
    Context context;
    private SQLiteDatabase database;
    private File path;
    private String databaseName;
    private String versionDataBase;

    /**
     * @param context
     */
    private Database(@NonNull Context context) {
        this.context = context;
    }

    public static Database getInstance() {
        return mInstance;
    }

    public static void init(@NonNull Context context) {
        if (mInstance == null) {
            mInstance = new Database(context);
            Database.getInstance().setVersionDataBase(context.getPackageName() + ".database_version");
        }
    }

    public String getVersionDataBase() {
        return versionDataBase;
    }

    private void setVersionDataBase(String versionDataBase) {
        this.versionDataBase = versionDataBase;

    }

    public Context getContext() {
        return context;
    }

    /**
     * @return
     */
    private String getDatabaseName() {
        return databaseName;
    }

    /**
     * @param databaseName
     */
    private void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @return
     */
    private File getFilePath() {
        return path;
    }

    /**
     *
     */
    private void setFilePath() {
        this.path = new File(getContext().getExternalFilesDir("db") + "/" + databaseName);
    }

    /**
     * @return
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }


    public void execSQL(String sql) {
        openDatabase();
        getDatabase().execSQL(sql);
        closeDatabase();
    }

    @Deprecated
    public boolean prepareDB() throws NoSuchMethodException {
        throw new NoSuchMethodException("This method was Deprecated, use prepareDatabase method");
    }

    public boolean prepareDatabaseAssets(@NonNull String databaseName, int version) {
        Cache.setContext(getContext());
        InputStream is = null;
        int currentVersion = Cache.get(getVersionDataBase(), 0);
        if (currentVersion > version) {
            throw new RuntimeException("can't downgrade database version");
        }
        setDatabaseName(databaseName);
        try {
            is = context.getAssets().open(getDatabaseName());
        } catch (IOException e) {
            Logger.w(TAG, e);
            return false;
        }
        setFilePath();
        if (currentVersion < version) {
            Cache.put(getVersionDataBase(), version);
            getFilePath().delete();
            return checkDatabase(is);
        } else {
            return checkDatabase(is);
        }
    }

    public boolean prepareDatabaseFile(@NonNull String path, @NonNull String databaseName, int version) {
        Cache.setContext(getContext());
        InputStream is = null;
        int currentVersion = Cache.get(getVersionDataBase(), 0);
        if (currentVersion > version) {
            throw new RuntimeException("can't downgrade database version");
        }
        setDatabaseName(databaseName);
        try {
            is = new FileInputStream(path + "/" + databaseName);
        } catch (IOException e) {
            Logger.w(TAG, e);
            return false;
        }
        setFilePath();
        if (currentVersion < version) {
            Cache.put(getVersionDataBase(), version);
            getFilePath().delete();
            return checkDatabase(is);
        } else {
            return checkDatabase(is);
        }
    }

    /**
     *
     */
    public void openDatabase() {
        database = SQLiteDatabase.openOrCreateDatabase(getFilePath(), null);
    }

    /**
     *
     */
    public void closeDatabase() {
        if (database != null) {
            try {
                database.close();
            } finally {
                database.close();
            }
        }
    }

    /**
     * @param is
     * @return
     */
    private boolean checkDatabase(InputStream is) {
        if (getFilePath().exists()) {
            database = SQLiteDatabase.openOrCreateDatabase(getFilePath(), null);
            Logger.d(TAG, "db path exists");
            closeDatabase();
            return true;
        } else {
            try {
                if (copy(new FileOutputStream(getFilePath()), is)) {
                    database = SQLiteDatabase.openOrCreateDatabase(getFilePath(), null);
                    Logger.d(TAG, "db path copy");
                    closeDatabase();
                    return true;
                } else {
                    return false;
                }
            } catch (FileNotFoundException e) {
                Logger.w(TAG, e);
                return false;
            }
        }
    }

    /**
     * @param os
     * @param is
     * @return
     */
    private boolean copy(@NonNull OutputStream os, @NonNull InputStream is) {
        try {
            int readied = 0;
            byte[] buffer = new byte[8 * 1024];
            while ((readied = is.read(buffer)) > 0) {
                os.write(buffer, 0, readied);
            }
            try {
                is.close();
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
            try {
                os.flush();
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
            try {
                os.close();
            } catch (Exception e) {
                Logger.w(TAG, e);
            }
            return true;
        } catch (Exception e) {
            Logger.w(TAG, e);
            return false;
        }
    }
}
