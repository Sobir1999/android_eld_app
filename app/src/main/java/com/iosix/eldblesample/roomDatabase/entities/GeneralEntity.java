package com.iosix.eldblesample.roomDatabase.entities;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.iosix.eldblesample.roomDatabase.converter.TrailerConverter;
import com.iosix.eldblesample.roomDatabase.converter.TrailerConverterString;

import java.util.ArrayList;

@Entity (tableName = "general")
public class GeneralEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "driver_id")
    private String driverId;
    @SerializedName("distance")
    @ColumnInfo(name = "distance")
    private String distance;
    @SerializedName("shipping_doc")
    @ColumnInfo(name = "shipping_docs")
    private ArrayList<String> shippingDocs;
    @SerializedName("vehicles")
    @ColumnInfo(name = "vehicles")
    private ArrayList<String> vehicle;
    @SerializedName("trailers")
    @ColumnInfo(name = "trailers")
    private ArrayList<String> trailers;
    @SerializedName("carrier")
    @ColumnInfo(name = "carrier")
    private String carrier;
    @SerializedName("main_ofice")
    @ColumnInfo(name = "main_office")
    private String mainOffice;
    @SerializedName("home_terminal_address")
    @ColumnInfo(name = "home_terminal_address")
    private String homrTerminalAddress;
    @SerializedName("co_driver")
    @ColumnInfo(name = "co_driver")
    private ArrayList<String> co_driver_name;
    @SerializedName("from_address")
    @ColumnInfo(name = "from_address")
    private String from_info;
    @SerializedName("to_address")
    @ColumnInfo(name = "to_address")
    private String to_info;
    @SerializedName("notes")
    @ColumnInfo(name = "notes")
    private String note;
    @SerializedName("day")
    @ColumnInfo(name = "day")
    private String day;
//    @ColumnInfo(name = "signature")
//    private Bitmap signature;


    public GeneralEntity(String driverId,String distance, ArrayList<String> shippingDocs, ArrayList<String> vehicle, ArrayList<String> trailers, String carrier, String mainOffice, String homrTerminalAddress, ArrayList<String> co_driver_name, String from_info, String to_info, String note, String day) {
        this.driverId = driverId;
        this.distance = distance;
        this.shippingDocs = shippingDocs;
        this.vehicle = vehicle;
        this.trailers = trailers;
        this.carrier = carrier;
        this.mainOffice = mainOffice;
        this.homrTerminalAddress = homrTerminalAddress;
        this.co_driver_name = co_driver_name;
        this.from_info = from_info;
        this.to_info = to_info;
        this.note = note;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<String> getShippingDocs() {
        return shippingDocs;
    }

    public void setShippingDocs(ArrayList<String> shippingDocs) {
        this.shippingDocs = shippingDocs;
    }

    public ArrayList<String> getVehicle() {
        return vehicle;
    }

    public void setVehicle(ArrayList<String> vehicle) {
        this.vehicle = vehicle;
    }

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getMainOffice() {
        return mainOffice;
    }

    public void setMainOffice(String mainOffice) {
        this.mainOffice = mainOffice;
    }

    public String getHomrTerminalAddress() {
        return homrTerminalAddress;
    }

    public void setHomrTerminalAddress(String homrTerminalAddress) {
        this.homrTerminalAddress = homrTerminalAddress;
    }

    public ArrayList<String> getCo_driver_name() {
        return co_driver_name;
    }

    public void setCo_driver_name(ArrayList<String> co_driver_name) {
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
