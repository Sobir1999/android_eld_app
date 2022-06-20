package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
public class UserData {

    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;
    public Context _context;

    public static final String PREF_NAME = "userData";

    public UserData(Context context) {
        this._context = context;
        userPref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

    public void saveAutoSwitch(boolean isAutoSwitch){
        editor = userPref.edit();
        editor.putBoolean("isAutoSwitch", isAutoSwitch);
        editor.apply();
    }

    public boolean getAutoSwitch(){
        return userPref.getBoolean("isAutoSwitch",false);
    }

}