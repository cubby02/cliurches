package com.cubbysulotions.cliurches.Utilities;

import android.os.Build;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {
    public static LocalDate selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formattedShortDate(String date){
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MMM d");
        Date stringDate = null;
        try {
            stringDate = format1.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return format2.format(stringDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String timeAgo(String stringDate, String stringTime){
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        try {
            long time = format1.parse(stringDate + " " + stringTime).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return (String) ago;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formattedTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return time.format(formatter);
    }
}
