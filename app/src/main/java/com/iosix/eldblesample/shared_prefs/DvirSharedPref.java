package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class DvirSharedPref {

    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;
    public Context _context;

    public static final String DVIR_PREF_NAME = "dvirdata";

    public DvirSharedPref(Context _context){
        userPref = _context.getSharedPreferences(DVIR_PREF_NAME,Context.MODE_PRIVATE);
    }

    public void saveDvir(String textValue){
        editor = userPref.edit();
        editor.putString("textValue",textValue);
        editor.apply();
    }

    public String getDVIR(){
        return userPref.getString("textValue","");
    }
}
