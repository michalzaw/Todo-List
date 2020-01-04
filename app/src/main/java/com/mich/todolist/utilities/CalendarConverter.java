package com.mich.todolist.utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michal on 08.11.2017.
 */

public class CalendarConverter {

    public static final String SIMPLE_DATE_FORMAT = "dd.MM.yyyy";
    public static final String SIMPLE_TIME_FORMAT = "HH:mm";
    public static final String DATE_AND_TIME_FORMAT = "dd.MM.yyyy HH:mm";


    public static String calendarToString(Calendar calendar, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(calendar.getTime());
    }

    public static Calendar stringToCalendar(String dateTime, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(dateTime));
            return calendar;
        } catch (ParseException e) {
            Log.e("ERROR", "Invalid date format");
            return Calendar.getInstance();
        }
    }
}
