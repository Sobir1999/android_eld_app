package com.iosix.eldblesample.roomDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dvir")
public class DvirEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "unit")
    private String unit;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "signature")
    private String signature;
    @ColumnInfo(name = "day")
    private String day;

    public DvirEntity(int id, String unit, String time, String location, String signature, String day) {
        this.id = id;
        this.unit = unit;
        this.time = time;
        this.location = location;
        this.signature = signature;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
