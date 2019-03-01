package ir.acharkit.android.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    5/4/18
 * Email:   alirezat775@gmail.com
 */

public class DateUtil {

    private static final String TAG = DateUtil.class.getName();

    public static PersianDate getPersianDate() {
        return DateConverter.civilToPersian(new CivilDate(makeCalendarFromDate(new Date())));
    }

    public static IslamicDate getIslamicDate() {
        return DateConverter.civilToIslamic(new CivilDate(makeCalendarFromDate(new Date())), -1);
    }

    public static CivilDate getCivilDate() {
        return new CivilDate(makeCalendarFromDate(new Date()));
    }

    private static Calendar makeCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
