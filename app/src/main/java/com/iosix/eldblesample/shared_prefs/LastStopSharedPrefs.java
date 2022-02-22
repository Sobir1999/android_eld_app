package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.iosix.eldblesample.R;

public class LastStopSharedPrefs {

    Context context;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public LastStopSharedPrefs(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE);
    }

    public void saveLastStopTime(int time){
        editor = sharedPreferences.edit();
        editor.putInt("LAST_STOP_TIME",time);
        editor.apply();
    }

    public void saveLastStopDate(String date){
        editor = sharedPreferences.edit();
        editor.putString("LAST_STOP_DATE",date);
        editor.apply();
    }

    public int getLastStopTime(){
        return sharedPreferences.getInt("LAST_STOP_TIME",0);
    }

    public String getLastStopDate(){
        return sharedPreferences.getString("LAST_STOP_DATE","");
    }
}
