package ir.acharkit.android.downloader;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    07/11/2017
 * Email:   alirezat775@gmail.com
 */
public interface OnDownloadListener {
    void onCompleted();
    void onFailure(String reason);
    void progressUpdate(int percent, int downloadedSize, int totalSize);
}
