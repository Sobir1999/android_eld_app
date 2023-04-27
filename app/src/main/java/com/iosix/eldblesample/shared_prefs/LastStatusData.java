package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;

import java.util.Calendar;

public class LastStatusData {

    private final SharedPreferences userPref;
    private SharedPreferences.Editor editor;
    public Context appContext;
    private static volatile LastStatusData instance;
    private static final Object LOCK = new Object();


    public static final String PREF_NAME = "userData";

    public static LastStatusData getInstance(Context applicationContext){
        if (instance == null){
            synchronized (LOCK){
                instance = new LastStatusData(applicationContext);
            }
        }
        return instance;
    }

    public LastStatusData(Context applicationContext) {
        this.appContext = applicationContext;
        userPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLasStatus(String status, int second,String day){
        editor = userPref.edit();
        editor.putString("status", status);
        editor.putString("day", day);
        editor.putInt("second", second);
        editor.apply();
    }

    public void saveLastDate(String date){
        editor = userPref.edit();
        editor.putString("date",date);
    }

    public void saveLastConnState(String state){
        editor = userPref.edit();
        editor.putString("state",state);
        editor.apply();
    }

    public void saveLastEngineState(String state){
        editor = userPref.edit();
        editor.putString("engine",state);
        editor.apply();
    }

    public void saveLastInspectionLockPassword(String password){
        editor = userPref.edit();
        editor.putString("password",password);
    }

    public String getLastInspectionLockPassword(){
        return userPref.getString("password","");
    }

    public String getLastStatus() {
        return userPref.getString("status", EnumsConstants.STATUS_OFF);
    }
    public String getLastConnState() {
        return userPref.getString("state", String.valueOf(R.string.not_connected));
    }

    public String getLastEngineState() {
        return userPref.getString("engine", String.valueOf(R.string.off));
    }

    public String getLastDay(){
        return userPref.getString("day","");
    }

    public int getLasStatSecond() {
        return userPref.getInt("second", 0);
    }

}
