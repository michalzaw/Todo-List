package com.mich.todolist.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michal on 08.11.2017.
 */

public class CalendarConverter {

    public static final String SIMPLE_DATE_FORMAT = "dd.MM.yyyy";
    public static final String SIMPLE_TIME_FORMAT = "HH:mm";


    public static String calendarToString(Calendar calendar, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(calendar.getTime());
    }
}
