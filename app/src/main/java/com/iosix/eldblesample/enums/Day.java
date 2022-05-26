package com.iosix.eldblesample.enums;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
}
