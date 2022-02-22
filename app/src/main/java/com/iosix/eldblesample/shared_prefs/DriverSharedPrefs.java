package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class DriverSharedPrefs {

    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;
    public Context _context;

    public static final String DRIVER_PREF_NAME = "driverdata";

    public DriverSharedPrefs(Context _context){
        userPref = _context.getSharedPreferences(DRIVER_PREF_NAME,Context.MODE_PRIVATE);
    }

    public void saveLastUsername(String firstName){
        editor = userPref.edit();
        editor.putString("firstName",firstName);
        editor.apply();
    }

    public void saveLastSurname(String surName){
        editor = userPref.edit();
        editor.putString("surName",surName);
        editor.apply();
    }

    public void saveLastPhoneNumber(String phoneNumber){
        editor = userPref.edit();
        editor.putString("phoneNumber",phoneNumber);
        editor.apply();
    }

    public void saveLastMainOffice(String mainOffice){
        editor = userPref.edit();
        editor.putString("mainOffice",mainOffice);
        editor.apply();
    }

    public void saveLastHomeTerAdd(String homeTerAdd){
        editor = userPref.edit();
        editor.putString("homeTerAdd",homeTerAdd);
        editor.apply();
    }

    public void saveLastHomeTerTimeZone(String homeTerTimezone){
        editor = userPref.edit();
        editor.putString("homeTerTimezone",homeTerTimezone);
        editor.apply();
    }

    public void saveLastImage(String image){
        editor = userPref.edit();
        editor.putString("image",image);
        editor.apply();
    }

    public String getFirstname(){
        return userPref.getString("firstName","");
    }

    public String getLastname(){
        return userPref.getString("surName","");
    }

    public String getPhoneNumber(){
        return userPref.getString("phoneNumber","");
    }

    public String getMainOffice(){
        return userPref.getString("mainOffice","");
    }

    public String getHomeTerAdd(){
        return userPref.getString("homeTerAdd","");
    }

    public String getHomeTerTimezone(){
        return userPref.getString("homeTerTimezone","");
    }

    public String getImage(){
        return userPref.getString("image","");
    }
}
