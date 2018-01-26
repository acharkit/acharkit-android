package ir.acharkit.android.component.cardList;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/22/17
 * Email:   alirezat775@gmail.com
 */
public interface CardListSnappingListener {
    void onPositionChange(int position);
    void onScroll(int dx, int dy);
}
