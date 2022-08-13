package com.iosix.eldblesample.enums;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Day {

    public static String getDayTime1(String time) {
//        String time = "" + Calendar.getInstance().getTime();
        return time.split(" ")[1] + " " + time.split(" ")[2];
    }

    public static String getDayName2(String time) {
//        String time = "" + Calendar.getInstance().getTime();
        return time.split(" ")[0];
    }

    public static String getCalculatedDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        Date newDate = cal.getTime();
        return "" + newDate;
    }

    public static int getCurrentSeconds() {
        int hour = Calendar.getInstance().getTime().getHours();
        int minute = Calendar.getInstance().getTime().getMinutes();
        int second = Calendar.getInstance().getTime().getSeconds();
        return hour * 3600 + minute * 60 + second;
    }

    public static String stringToDate(String s) throws ParseException {
        DateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        DateFormat mediumFormat = new SimpleDateFormat("yyyy MMM dd",Locale.getDefault());
        return shortFormat.format(mediumFormat.parse(Calendar.getInstance().get(Calendar.YEAR) + " " + s));
    }

    public static String dateToString(String s) throws ParseException {
        DateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        DateFormat format = new SimpleDateFormat("MMM dd",Locale.getDefault());
        return format.format(shortFormat.parse(s));
    }

    public static String intToTime(int i){
        int hour = i/3600;
        int minut = (i - hour*3600)/60;
        if (hour<10){
            if (minut < 10){
                return String.format("0%sh 0%sm",hour,minut);
            }else {
                return String.format("0%sh %sm",hour,minut);
            }
        }else {
            if (minut < 10){
                return String.format("%sh 0%sm",hour,minut);
            }else {
                return String.format("%sh %sm",hour,minut);
            }
        }
    }

    public static String getCurrentTime(int seconds){
        int hour = seconds/3600;
        int minut = (seconds - hour*3600)/60;

        if (hour > 12){
            if (hour-12 < 10){
                if (minut < 10){
                    return String.format("0%s:0%s",hour-12,minut) + " PM CDT";
                }else {
                    return String.format("0%s:%s",hour-12,minut) + " PM CDT";
                }
            }else {
                if (minut < 10){
                    return String.format("%s:0%s",hour-12,minut) + " PM CDT";
                }else {
                    return String.format("%s:%s",hour-12,minut) + " PM CDT";
                }
            }
        }else {
            if (hour < 10){
                if (minut < 10){
                    return String.format("0%s:0%s",hour,minut) + " AM CDT";
                }else {
                    return String.format("0%s:%s",hour,minut) + " AM CDT";
                }
            }else {
                if (minut < 10){
                    return String.format("%s:0%s",hour,minut) + " AM CDT";
                }else {
                    return String.format("%s:%s",hour,minut) + " AM CDT";
                }
            }
        }
    }
}
