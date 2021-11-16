package com.iosix.eldblesample.roomDatabase.entities;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "general")
public class GeneralEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "driver_name")
    private String driver_name;
    @ColumnInfo(name = "distance")
    private String distance;
    @ColumnInfo(name = "vehicle")
    private String vehicle;
    @ColumnInfo(name = "trailer1")
    private String trailer1;
    @ColumnInfo(name = "trailer2")
    private String trailer2;
    @ColumnInfo(name = "co_driver_name")
    private String co_driver_name;
    @ColumnInfo(name = "from_info")
    private String from_info;
    @ColumnInfo(name = "to_info")
    private String to_info;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "signature")
    private Bitmap signature;
    @ColumnInfo (name = "day")
    private String day;

    public GeneralEntity(String driver_name, String distance, String vehicle, String trailer1, String trailer2, String co_driver_name, String from_info, String to_info, String note, Bitmap signature, String day) {
//        this.id = id;
        this.driver_name = driver_name;
        this.distance = distance;
        this.vehicle = vehicle;
        this.trailer1 = trailer1;
        this.trailer2 = trailer2;
        this.co_driver_name = co_driver_name;
        this.from_info = from_info;
        this.to_info = to_info;
        this.note = note;
        this.signature = signature;
        this.day = day;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getTrailer1() {
        return trailer1;
    }

    public void setTrailer1(String trailer1) {
        this.trailer1 = trailer1;
    }

    public String getTrailer2() {
        return trailer2;
    }

    public void setTrailer2(String trailer2) {
        this.trailer2 = trailer2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCo_driver_name() {
        return co_driver_name;
    }

    public void setCo_driver_name(String co_driver_name) {
        this.co_driver_name = co_driver_name;
    }

    public String getFrom_info() {
        return from_info;
    }

    public void setFrom_info(String from_info) {
        this.from_info = from_info;
    }

    public String getTo_info() {
        return to_info;
    }

    public void setTo_info(String to_info) {
        this.to_info = to_info;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Bitmap getSignature() {
        return signature;
    }

    public void setSignature(Bitmap signature) {
        this.signature = signature;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
