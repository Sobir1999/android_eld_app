package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class GeneralSharedPrefs {

    public static SharedPreferences userPref;
    public static SharedPreferences.Editor editor;
    public Context _context;

    public static final String GENERAL_PREF_NAME = "generaldata";


    public GeneralSharedPrefs(Context context) {
        this._context = context;
        userPref = _context.getSharedPreferences(GENERAL_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLasVehicle(String vehicleId){
        editor = userPref.edit();
        editor.putString("Vehicle",vehicleId);
        editor.apply();
    }

    public void saveLasTrailer(String trailerId){
        editor = userPref.edit();
        editor.putString("Trailer",trailerId);
        editor.apply();
    }

    public void saveLasFromDes(String fromDes){
        editor = userPref.edit();
        editor.putString("fromDestination",fromDes);
        editor.apply();
    }

    public void saveLasToDes(String toDes){
        editor = userPref.edit();
        editor.putString("toDestination",toDes);
        editor.apply();
    }

    public void saveLasNote(String note){
        editor = userPref.edit();
        editor.putString("note",note);
        editor.apply();
    }

    public String getLastVehicle() {
        return userPref.getString("Vehicle","");
    }

    public String getLastTrailer() {
        return userPref.getString("Trailer","");
    }

    public String getLastFromDes() {
        return userPref.getString("fromDestination","");
    }

    public String getLastToDes() {
        return userPref.getString("toDestination","");
    }

    public String getLastNotes() {
        return userPref.getString("note","");
    }

    public void saveCoDriver(String coDriver) {
        editor = userPref.edit();
        editor.putString("coDriver",coDriver);
        editor.apply();
    }

    public String getCoDriver(){
        return userPref.getString("coDriver","");
    }
}
