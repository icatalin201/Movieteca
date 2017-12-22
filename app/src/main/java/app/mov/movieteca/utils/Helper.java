package app.mov.movieteca.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Catalin on 12/22/2017.
 */

public class Helper {

    public static String formatDate(String dateString){
        Date date = null;
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }
}
