package com.iosix.eldblesample.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "dvir_table", indices = {@Index(value = {"time"}, unique = true)})
public class Dvir {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "satisfy")
    @SerializedName("satisfy")
    boolean isSatisfy;

    @ColumnInfo(name = "unit")
    @SerializedName("unit")
    String unit;

    @ColumnInfo(name = "trailer")
    @SerializedName("trailer")
    ArrayList<String> trailers;

    @ColumnInfo(name = "defect")
    @SerializedName("defact")
    ArrayList<String> defects;

    @ColumnInfo(name = "note")
    @SerializedName("notes")
    String note;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    String time;

    @ColumnInfo(name = "location")
    @SerializedName("point")
    String point;

    @ColumnInfo(name = "created_date")
    @SerializedName("created_date")
    String date;

    public Dvir(boolean isSatisfy,String unit, ArrayList<String> trailers, ArrayList<String> defects, String note, String time, String point,String date) {
        this.isSatisfy = isSatisfy;
        this.unit = unit;
        this.trailers = trailers;
        this.defects = defects;
        this.note = note;
        this.time = time;
        this.point = point;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSatisfy() {
        return isSatisfy;
    }

    public void setSatisfy(boolean satisfy) {
        isSatisfy = satisfy;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
