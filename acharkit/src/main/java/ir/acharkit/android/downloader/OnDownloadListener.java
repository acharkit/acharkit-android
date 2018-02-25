package ir.acharkit.android.downloader;

import java.io.File;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    07/11/2017
 * Email:   alirezat775@gmail.com
 */
public interface OnDownloadListener {
    void onCompleted(File file);
    void onFailure(String reason);
    void progressUpdate(int percent, int downloadedSize, int totalSize);
}
