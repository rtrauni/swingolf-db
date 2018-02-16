package at.hallermayr.swingolf.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by rudolftraunmueller on 16.02.2018.
 */
public class DateUtil {
    private static DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    static Long getToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        return Long.valueOf(formatter.format(calendar.getTime()));
    }
}
