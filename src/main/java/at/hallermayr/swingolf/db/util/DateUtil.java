package at.hallermayr.swingolf.db.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by rudolftraunmueller on 16.02.2018.
 */
public class DateUtil {
    private static DateFormat yyyyMMdd_Formatter = new SimpleDateFormat("yyyyMMdd");

    public static Long getTodayAsLong() {
        GregorianCalendar calendar = new GregorianCalendar();
        return Long.valueOf(yyyyMMdd_Formatter.format(calendar.getTime()));
    }
}
