package ir.acharkit.android.component.roster;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/22/17
 * Email:   alirezat775@gmail.com
 */
public interface RosterListener {
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
