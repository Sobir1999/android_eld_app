package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {

    public static SharedPreferences callUserPrefs(Context context) {
        return context.getSharedPreferences(UserData.PREF_NAME, MODE_PRIVATE);
    }
}
