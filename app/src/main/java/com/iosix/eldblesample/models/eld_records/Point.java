package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Point {

    @SerializedName("type")
    String type;

    @SerializedName("coordinates")
    ArrayList<Double> arrayList;

    public Point(String type, ArrayList<Double> arrayList) {
        this.type = type;
        this.arrayList = arrayList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Double> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Double> arrayList) {
        this.arrayList = arrayList;
    }
}
