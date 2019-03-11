package ir.acharkit.android.util.helper;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ir.acharkit.android.util.Logger;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/3/2017
 * Email:   alirezat775@gmail.com
 */
public class DateTimeHelper {

    private static final String TAG = DateTimeHelper.class.getName();
    private static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());

    /**
     * @return
     */
    public static String currentDateTime(String timeZone) {
        Date date = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateFormat.format(date);
    }

    /**
     * @param time
     * @return
     */
    public static long dateStringToMillis(@NonNull final String time) {
        return dateStringToMillis(time, dateFormat);
    }

    /**
     * @param time
     * @param format
     * @return
     */
    public static long dateStringToMillis(@NonNull final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            Logger.w(TAG, e);
        }
        return -1;
    }

    /**
     * @param millis
     * @return
     */
    public static String millisToStringDate(final long millis) {
        return millisToStringDate(millis, dateFormat);
    }

    /**
     * @param millis
     * @param format
     * @return
     */
    public static String millisToStringDate(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }
}
