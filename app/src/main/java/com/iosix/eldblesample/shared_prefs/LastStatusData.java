package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class LastStatusData {

    private SharedPreferences userPref;
    private SharedPreferences.Editor editor;
    public Context appContext;
    public static LastStatusData instance;

    public static final String PREF_NAME = "userData";

    public static synchronized LastStatusData getInstance(Context applicationContext){
        if (instance == null){
            instance = new LastStatusData(applicationContext);
        }
        return instance;
    }

    public LastStatusData(Context applicationContext) {
        this.appContext = applicationContext;
        userPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLasStatus(int status, int second){
        editor = userPref.edit();
        editor.putInt("status", status);
        editor.putInt("second", second);
        editor.apply();
    }

    public void saveLastDate(String date){
        editor = userPref.edit();
        editor.putString("date",date);
    }

    public void saveLastEldState(String state){
        editor = userPref.edit();
        editor.putString("state",state);
    }

    public String getLastEldState(){
        return userPref.getString("state","Not Connected");
    }

    public int getLastStatus() {
        return userPref.getInt("status", 0);
    }



    public String getLastDate(){
        return userPref.getString("date","");
    }

    public int getLasStatSecond() {
        return userPref.getInt("second", 0);
    }

}
