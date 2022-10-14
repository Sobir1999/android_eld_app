package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.iosix.eldblesample.R;

public class LastStopSharedPrefs {

    private static volatile LastStopSharedPrefs INSTANCE = null;
    private static final Object LOCK = new Object();

    private final SharedPreferences preferences;

    public static LastStopSharedPrefs getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new LastStopSharedPrefs(context);
                }
            }
        }

        return INSTANCE;
    }

    private LastStopSharedPrefs(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveLastStopTime(int time) {
        Editor editor = preferences.edit();
        editor.putInt("LAST_STOP_TIME", time);
        editor.apply();
    }

    public void saveLastStopDate(String date) {
        Editor editor = preferences.edit();
        editor.putString("LAST_STOP_DATE", date);
        editor.apply();
    }

    public int getLastStopTime() {
        return preferences.getInt("LAST_STOP_TIME", 0);
    }

    public String getLastStopDate() {
        return preferences.getString("LAST_STOP_DATE", "");
    }
}
