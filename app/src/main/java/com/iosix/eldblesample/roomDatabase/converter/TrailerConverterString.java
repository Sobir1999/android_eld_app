package com.iosix.eldblesample.roomDatabase.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iosix.eldblesample.models.eld_records.Point;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrailerConverterString {

    @TypeConverter
    public static ArrayList<String> toTrailnumber(String value){
        if (value == null) {
            return (null);
        }
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String  toString(ArrayList<String> arrayList){
        if (arrayList == null) {
            return (null);
        }
        Gson gson = new Gson();
        return gson.toJson(arrayList);
    }

}
