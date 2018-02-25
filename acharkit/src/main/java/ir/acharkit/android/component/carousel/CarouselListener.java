package ir.acharkit.android.component.carousel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/22/17
 * Email:   alirezat775@gmail.com
 */
public interface CarouselListener {
    /**
     * @param position current position
     */
    void onPositionChange(int position);

    /**
     * @param dx delta x
     * @param dy delta y
     */
    void onScroll(int dx, int dy);
}
