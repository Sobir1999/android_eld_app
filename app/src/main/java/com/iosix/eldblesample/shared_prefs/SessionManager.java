package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.iosix.eldblesample.R;

public class SessionManager {

    Context context;
    private static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String USER_ACCESS_TOKEN = "user_access_token";
    public static final String USER_TOKEN = "user_token";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String token){
        editor = sharedPreferences.edit();
        editor.putString(USER_ACCESS_TOKEN,token);
        editor.apply();
    }

    public void saveToken(String token){
        editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN,token);
        editor.apply();
    }

    public String fetchAccessToken(){
        return sharedPreferences.getString(USER_ACCESS_TOKEN, null);
    }

    public String fetchToken(){
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public void clearAccessToken(){
        editor = sharedPreferences.edit();
        editor.remove(USER_ACCESS_TOKEN);
        editor.commit();
    }

    public void clearToken(){
        editor = sharedPreferences.edit();
        editor.remove(USER_TOKEN);
        editor.commit();
    }
}
