package ir.acharkit.android.component.indicator;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    07/11/2017
 * Email:   alirezat775@gmail.com
 */
public interface OnPageChangeListener {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);
}