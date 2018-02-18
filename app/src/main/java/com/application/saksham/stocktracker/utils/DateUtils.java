package com.application.saksham.stocktracker.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Saksham Dhawan on 2/17/18.
 */

public class DateUtils {

    public static Date convertStringToDate(String dateString) {
        Date date = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static String convertDateToString(Date date) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateString = sdfr.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }

    public static Calendar getCurrentTimeInUs() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5:00"));
        return cal;
    }

    public static boolean isMarkedOpen() {
        Calendar dateInUs = DateUtils.getCurrentTimeInUs();
        int currentHour = dateInUs.get(Calendar.HOUR_OF_DAY);
        int currentDayOfWeek = dateInUs.get(Calendar.DAY_OF_WEEK);
        return currentDayOfWeek >= Calendar.MONDAY && currentDayOfWeek <= Calendar.FRIDAY && currentHour >= 9 && currentHour <= 16;
    }
}
