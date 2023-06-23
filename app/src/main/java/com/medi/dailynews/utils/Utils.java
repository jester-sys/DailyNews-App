package com.medi.dailynews.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static final String API_KEY = "865b1327b8f84c8c89078cc7e95074ba";

    public static String DateToTimeFormat(String existingStringDate) {


        PrettyTime prettyTime = new PrettyTime();

        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = simpleDateFormat.parse(existingStringDate);
            time = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String DateFormat(String existingStringDate) {
        String newDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy");
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(existingStringDate);
            newDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = existingStringDate;
        }

        return newDate;
    }

}
