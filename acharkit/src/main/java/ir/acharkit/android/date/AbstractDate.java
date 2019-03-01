package ir.acharkit.android.date;

/**
 * <p>
 * Abstract class representing a date. Instances of this class should be
 * mutable. Varios getters and setters are provided so that date manipulation is
 * as convenient as possible.
 * <p>
 * Forked from https://github.com/ebraminio/DroidPersianCalendar/tree/master/PersianCalendar/src/main/java/calendar
 *
 * @author Amir
 * @author ebraminio
 */
public abstract class AbstractDate {

    public final static String DAY = "day";
    public final static String IS_OUT_OF_RANGE = "is out of range!";
    public final static String NOT_IMPLEMENTED_YET = "not implemented yet!";
    public final static String MONTH = "month";
    public final static String YEAR_0_IS_INVALID = "Year 0 is invalid!";

    public void setDate(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(day);
    }

    public abstract int getYear();

    public abstract void setYear(int year);

    public abstract int getMonth();

    public abstract void setMonth(int month);

    public abstract int getDayOfMonth();

    public abstract void setDayOfMonth(int day);

    public abstract int getDayOfWeek();

    public abstract int getDayOfYear();

    public abstract int getWeekOfYear();

    public abstract int getWeekOfMonth();

    public abstract void rollDay(int amount, boolean up);

    public abstract void rollMonth(int amount, boolean up);

    public abstract void rollYear(int amount, boolean up);

    /**
     * Returns a string specifying the event of this date, or null if there are
     * no events for this year.
     */
    public abstract String getEvent();

    public abstract boolean isLeapYear();

    public abstract AbstractDate clone();

    public static class DayOutOfRangeException extends RuntimeException {
        private static final long serialVersionUID = -9053871584605015203L;

        public DayOutOfRangeException() {
            super();
        }

        public DayOutOfRangeException(String arg0) {
            super(arg0);
        }
    }

    public static class YearOutOfRangeException extends RuntimeException {
        private static final long serialVersionUID = -9154217686200590192L;

        public YearOutOfRangeException() {
            super();
        }

        public YearOutOfRangeException(String arg0) {
            super(arg0);
        }
    }

    public static class MonthOutOfRangeException extends RuntimeException {
        private static final long serialVersionUID = 1871328381608677472L;

        public MonthOutOfRangeException() {
            super();
        }

        public MonthOutOfRangeException(String arg0) {
            super(arg0);
        }
    }
}