package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.iosix.eldblesample.R;

public class SessionManager {

    public static volatile SessionManager INSTANCE;
    private static final Object LOCK = new Object();
    private final SharedPreferences sharedPreferences;

    public static final String USER_ACCESS_TOKEN = "user_access_token";
    public static final String USER_TOKEN = "user_token";

    public static SessionManager getInstance(Context applicationContext) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionManager(applicationContext);
                }
            }
        }

        return INSTANCE;
    }

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(USER_ACCESS_TOKEN, token);
        editor.apply();
    }

    public void saveToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String fetchAccessToken() {
        return sharedPreferences.getString(USER_ACCESS_TOKEN, null);
    }
    public String fetchToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public void clearAccessToken() {
        Editor editor = sharedPreferences.edit();
        editor.remove(USER_ACCESS_TOKEN);
        editor.apply();
    }

    public void clearToken() {
        Editor editor = sharedPreferences.edit();
        editor.remove(USER_TOKEN);
        editor.apply();
    }
}
