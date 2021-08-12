package com.iosix.eldblesample.enums;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Day {

    public static String getDayTime() {
        String time = "" + Calendar.getInstance().getTime();
        return time.split(" ")[1] + " " + time.split(" ")[2];
    }

    public static String getDayName() {
        String time = "" + Calendar.getInstance().getTime();
        return time.split(" ")[0];
    }
}
