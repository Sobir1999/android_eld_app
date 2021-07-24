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

    public void saveMode(boolean isDark){
        editor = userPref.edit();
        editor.putBoolean("isDark", isDark);
        editor.apply();
    }

    public boolean getMode(){
        return userPref.getBoolean("isDark",false);
    }

    public void saveUserData(String name, String surname){
        editor = userPref.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.apply();
    }

}