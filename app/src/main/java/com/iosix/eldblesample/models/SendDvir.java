package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendDvir {

    @SerializedName("unit")
    String unit;

    @SerializedName("trailer")
    ArrayList<String> trailers;

    @SerializedName("defect")
    ArrayList<String> defects;

    @SerializedName("notes")
    String note;

    @SerializedName("time")
    String time;

    @SerializedName("point")
    String point;

    public SendDvir(String unit, ArrayList<String> trailers, ArrayList<String> defects, String note, String time, String point) {
        this.unit = unit;
        this.trailers = trailers;
        this.defects = defects;
        this.note = note;
        this.time = time;
        this.point = point;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<String> getDefects() {
        return defects;
    }

    public void setDefects(ArrayList<String> defects) {
        this.defects = defects;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
