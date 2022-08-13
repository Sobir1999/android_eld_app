package com.iosix.eldblesample.roomDatabase.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.roomDatabase.entities.TrailerId;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TrailerConverter {

    @TypeConverter
    public static String toString(ArrayList<TrailerId> value){
        if (value== null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<TrailerId>>() {}.getType();
        return gson.toJson(value,type);
    }

    @TypeConverter
    public static Point toPoint(String value){
        if (value == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Point>() {
        }.getType();
        return gson.fromJson(value,type);
    }

    @TypeConverter
    public static String toDouble(Point value){
        if (value== null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Point>() {}.getType();
        return gson.toJson(value,type);
    }
}
