package com.iosix.eldblesample.roomDatabase.converter;

import androidx.room.TypeConverter;

import com.iosix.eldblesample.models.eld_records.Point;

import java.util.ArrayList;

public class TrailerConverterString {

    @TypeConverter
    public ArrayList<String> toTrailnumber(String string){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(string);
        return arrayList;
    }

    @TypeConverter
    public String  toString(ArrayList<String> arrayList){
        if (arrayList.size()>0){
            return arrayList.get(0);
        }else {
            return null;
        }
    }

    @TypeConverter
    public ArrayList<Boolean> toEngineState(Boolean value){
        ArrayList<Boolean> arrayList = new ArrayList<>();
        arrayList.add(value);
        return arrayList;
    }

    @TypeConverter
    public Boolean toBoolean(ArrayList<Boolean> arrayList){
        return arrayList.get(0);
    }

    @TypeConverter
    public ArrayList<Double> toDoubleArray(Double value){
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(value);
        return arrayList;
    }

    @TypeConverter
    public Double toDouble(ArrayList<Double> arrayList){
        return arrayList.get(0);
    }

    @TypeConverter
    public ArrayList<Integer> toIntArray(int value){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(value);
        return arrayList;
    }

    @TypeConverter
    public int toPoint(ArrayList<Integer> arrayList){
        return arrayList.get(0);
    }

}
