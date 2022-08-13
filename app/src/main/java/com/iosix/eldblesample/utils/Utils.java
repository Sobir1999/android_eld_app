package com.iosix.eldblesample.utils;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.iosix.eldblelib.EldEngineStates;
import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.eld_records.Point;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Utils {

    public static Point hasCoordinates(Double latitude,Double longtitude){
        if (latitude == -1 || longtitude == -1 || latitude == 0 || longtitude == 0){
            return null;
        }else {
            ArrayList<Double> arrayList = new ArrayList<>();
            arrayList.add(longtitude);
            arrayList.add(latitude);
            return new Point("Point",arrayList);
        }
    }

    public static String getStatus(int status){
        if (status == EnumsConstants.STATUS_OFF){
            return "OFF";
        }else if (status == EnumsConstants.STATUS_SB){
            return "SB";
        }else if (status == EnumsConstants.STATUS_DR){
            return "D";
        }else if (status == EnumsConstants.STATUS_ON){
            return "ON";
        }
        else if (status == EnumsConstants.STATUS_OF_PC){
            return "PC";
        }
        else if (status == EnumsConstants.STATUS_ON_YM){
            return "YM";
        }
        else if (status == EnumsConstants.POWER_UP){
            return "POWERUP";
        }
        else if (status == EnumsConstants.POWER_DOWN){
            return "POWERDOW";
        }
        else if (status == EnumsConstants.LOGIN){
            return "LOGIN";
        }else if (status == EnumsConstants.LOGOUT){
            return "LOGOUT";
        }else {
            return "CERTIFIED";
        }
    }

    public static String getDateFormat(Date date){
        return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())).format(date);
    };

    public static String hasVin(String value){
        if (value.equals("")){
            return "vin";
        }else {
            return value;
        }
    }

    public static Boolean engineState(EldEngineStates state){
        return state == EldEngineStates.ENGINE_ON;
    }

    public static String defects(ArrayList<String> selectedList){
        StringBuilder defects = new StringBuilder();
        for(int i = 0; i < selectedList.size(); i++){
            defects.append(selectedList.get(i)).append("\n");
        }
        return defects.toString();
    }

    public static String pointToString(Context context,Point point) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (point.getArrayList().get(0) != 0 && point.getArrayList().get(1) != 0){
            List<Address> addresses = geocoder.getFromLocation(point.getArrayList().get(0), point.getArrayList().get(1), 1);
            Address obj = addresses.get(0);
            return obj.getAddressLine(0);
        }else {
            return "";
        }
    }

    public static final String[] BLUETOOTH_PERMISSIONS_S = { Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT} ;

}
