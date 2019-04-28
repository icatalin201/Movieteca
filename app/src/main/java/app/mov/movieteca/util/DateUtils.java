package app.mov.movieteca.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String STANDARD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String INVERSE_DATE_FORMAT = "dd-MM-yyyy";


    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date convertStringToDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateFromFormatToFormat(String date, String initialFormat, String targetFormat) {
        Date date1 = convertStringToDate(date, initialFormat);
        return convertDateToString(date1, targetFormat);
    }

}
