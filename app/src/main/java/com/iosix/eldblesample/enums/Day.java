package com.iosix.eldblesample.enums;

import static com.iosix.eldblesample.utils.Utils.getDateFormat;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Day {

    public static String getDayTime1(String time) {
        return time.split(" ")[1] + " " + time.split(" ")[2];
    }

    public static String getDayTime2(String time) {
        return time.split(" ")[0];
    }

    public static ZonedDateTime getCalculatedDate(int days) {
        ZonedDateTime now = ZonedDateTime.now();
        return now.minusDays(days);
    }

    public static ZonedDateTime getCalculatedDateBeforeCur(ZonedDateTime time,int days) {
        return time.minusDays(days);
    }

    public static String getDayFormat(ZonedDateTime date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(),
                date.getHour(), date.getMinute(), date.getSecond());
        Date dateToFormat = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd",Locale.getDefault());
        return formatter.format(dateToFormat);
    }

    public static String changeFormat(String s) {
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy MMM dd",Locale.getDefault());
        Date date = null;
        try {
            date = sourceDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        assert date != null;
        return targetDateFormat.format(date);
    }

    public static ZonedDateTime stringToDate(String s) {
        String regex1 = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{4}";
        String regex2 = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{6}[+-]\\d{3,4}";
        if (s.matches(regex1)){
            return ZonedDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        }else {
            return ZonedDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSxxx"));
        }
    }

    public static String stringToDay(String s) {
        for (int i = 0; i < 8; i++) {
            if (getDayFormat(ZonedDateTime.now().minusDays(i)).equals(s)){
                return ZonedDateTime.now().minusDays(i).toLocalDate().toString();
            }
        }
        return null;
    }

    public static String stringToDateTime(String s) {
        for (int i = 0; i < 8; i++) {
            if (getDayFormat(ZonedDateTime.now().minusDays(i)).equals(s)){
                Calendar calendar = Calendar.getInstance();
                calendar.set(ZonedDateTime.now().minusDays(i).getYear(), ZonedDateTime.now().minusDays(i).getMonthValue() - 1, ZonedDateTime.now().minusDays(i).getDayOfMonth(),
                        ZonedDateTime.now().minusDays(i).getHour(), ZonedDateTime.now().minusDays(i).getMinute(), ZonedDateTime.now().minusDays(i).getSecond());
                Date dateToFormat = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",Locale.getDefault());
                return formatter.format(dateToFormat);
            }
        }
        return null;
    }

    public static String getDayTimeFromZ(ZonedDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
        String formattedDate = time.format(formatter);
        return formattedDate.split(" ")[1] + " " + formattedDate.split(" ")[2];
    }

    public static int getCurrentMillis() {
        return LocalTime.now().toSecondOfDay();
    }

    public static String intToTime(int i){
        long hour = i/3600;
        long minut = (i - hour*3600)/60;
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
