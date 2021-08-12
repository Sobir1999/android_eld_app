package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class LastStatusData {

    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;
    public Context _context;

    public static final String PREF_NAME = "userData";

    public LastStatusData(Context context) {
        this._context = context;
        userPref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLasStatus(int status, int second){
        editor = userPref.edit();
        editor.putInt("status", status);
        editor.putInt("second", second);
        editor.apply();
    }

    public int getLastStatus() {
        return userPref.getInt("status", 0);
    }

    public int getLasStatSecond() {
        return userPref.getInt("second", 0);
    }
}
