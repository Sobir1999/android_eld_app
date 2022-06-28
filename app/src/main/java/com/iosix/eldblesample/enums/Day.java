package com.iosix.eldblesample.enums;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String stringToDate(String s) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy",Locale.getDefault());
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.parse(s + " " + Calendar.getInstance().get(Calendar.YEAR)).toInstant().atZone(ZoneId.systemDefault()).toString();
    }

    public static String intToTime(int i){
        int hour = i/3600;
        int minut = (i - hour*3600)/60;
        if (hour < 10 && minut < 10){
            return "0" + hour + ":" + "0" + minut;
        }else if (hour < 10){
            return "0" + hour + ":" + minut;
        }else if (minut < 10){
            return hour + ":" + "0" + minut;
        }else {
            return hour + ":" + minut;
        }
    }
}
