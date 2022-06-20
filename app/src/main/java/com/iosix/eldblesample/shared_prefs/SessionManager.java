package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.iosix.eldblesample.R;

import java.io.ByteArrayOutputStream;

public class SessionManager {

    Context appContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static SessionManager instance;
    public static final String USER_ACCESS_TOKEN = "user_access_token";
    public static final String USER_TOKEN = "user_token";

    public static synchronized SessionManager getInstance(Context applicationContext){
        if (instance == null){
            instance = new SessionManager(applicationContext);
        }
        return instance;
    }

    public SessionManager(Context applicationContext){
        appContext = applicationContext;
        sharedPreferences = appContext.getSharedPreferences(appContext.getString(R.string.app_name),Context.MODE_PRIVATE);
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

    public void saveEmail(String email){
        editor = sharedPreferences.edit();
        editor.putString("Email",email);
        editor.apply();
    }

    public void savePassword(String password){
        editor = sharedPreferences.edit();
        editor.putString("Password",password);
        editor.apply();
    }

    public String getemail(){
        return sharedPreferences.getString("Email","");
    }

    public String getPassword(){
        return sharedPreferences.getString("Password","");
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
