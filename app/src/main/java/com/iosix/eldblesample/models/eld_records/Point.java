package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Point {

    @SerializedName("type")
    final String type = "Point";

    @SerializedName("coordinates")
    ArrayList<Double> arrayList;

    public Point(ArrayList<Double> arrayList) {
        this.arrayList = arrayList;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Double> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Double> arrayList) {
        this.arrayList = arrayList;
    }
}
