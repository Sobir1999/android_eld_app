package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
public class UserData {

    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;
    public Context _context;
    private static final Object LOCK = new Object();


    public static final String PREF_NAME = "userData";

    public UserData(Context context) {
        this._context = context;
        synchronized (LOCK){
            userPref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public Object getByKey(String key){
        return userPref.getString(key,"");
    }

    public void saveLang(String lang){
        editor = userPref.edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    public String getLang(){
        return userPref.getString("lang","en");
    }

    public void addDefects(int defectType, String defect){
        editor = userPref.edit();
        editor.putString("defect-" + defectType, defect);
        editor.apply();
    }

    public String getDefects(int defectType){
        return userPref.getString("defect-" + defectType,"");
    }

    public void clearDefects(int defectType){
        editor = userPref.edit();
        editor.remove("defect-" + defectType);
        editor.apply();
    }

    public void saveMode(boolean isDark){
        editor = userPref.edit();
        editor.putBoolean("isDark", isDark);
        editor.apply();
    }

    public boolean getMode(){
        return userPref.getBoolean("isDark",false);
    }

    public void saveStartTime(int startTime){
        editor = userPref.edit();
        editor.putInt("startTime", startTime);
        editor.apply();
    }

    public void saveStartMin(int startMin){
        editor = userPref.edit();
        editor.putInt("startMin", startMin);
        editor.apply();
    }

    public int getStartTime(){
        return userPref.getInt("startTime",10);
    }

    public int getStartMin(){
        return userPref.getInt("startMin",0);
    }

    public void saveEndTime(int endTime){
        editor = userPref.edit();
        editor.putInt("endTime", endTime);
        editor.apply();
    }

    public void saveEndMin(int endMin){
        editor = userPref.edit();
        editor.putInt("endMin", endMin);
        editor.apply();
    }

    public int getEndTime(){
        return userPref.getInt("endTime",6);
    }

    public int getEndMin(){
        return userPref.getInt("endMin",0);
    }

    public void saveAutoSwitch(boolean isAutoSwitch){
        editor = userPref.edit();
        editor.putBoolean("isAutoSwitch", isAutoSwitch);
        editor.apply();
    }

    public boolean getAutoSwitch(){
        return userPref.getBoolean("isAutoSwitch",false);
    }

}