package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class TinyDB{

    private final SharedPreferences preferences;
    private static final Object LOCK = new Object();

    public TinyDB(Context appContext){
        synchronized (LOCK){
            preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        }
    }
    public void putListString(int key, ArrayList<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString("defect-" + key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public ArrayList<String> getListString(int key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString("defect-" + key, ""), "‚‗‚")));
    }

}
