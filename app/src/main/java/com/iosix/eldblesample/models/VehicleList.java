package com.iosix.eldblesample.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "vehicles")
public class VehicleList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @ColumnInfo(name = "vehicle_id")
    @SerializedName("vehicle_id")
    String vehicle_id;

    @ColumnInfo(name = "make")
    @SerializedName("make")
    String make;

    @ColumnInfo(name = "model")
    @SerializedName("model")
    String model;

    @ColumnInfo(name = "year")
    @SerializedName("year")
    String year;

    @ColumnInfo(name = "license_plate_num")
    @SerializedName("license_plate_num")
    String license_plate_num;

    @ColumnInfo(name = "license_plate_issue_state")
    @SerializedName("license_plate_issue_state")
    String license_plate_issue_state;

    @ColumnInfo(name = "vin")
    @SerializedName("vin")
    String vin;

    @ColumnInfo(name = "notes_vehicle")
    @SerializedName("notes_vehicle")
    String notes_vehicle;

    @ColumnInfo(name = "eld_id")
    @SerializedName("eld_id")
    String eld_id;

    @ColumnInfo(name = "company")
    @SerializedName("company")
    String company;

    public VehicleList(int id, String vehicle_id, String make, String model, String year, String license_plate_num, String license_plate_issue_state, String vin, String notes_vehicle, String eld_id, String company) {
        this.id = id;
        this.vehicle_id = vehicle_id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.license_plate_num = license_plate_num;
        this.license_plate_issue_state = license_plate_issue_state;
        this.vin = vin;
        this.notes_vehicle = notes_vehicle;
        this.eld_id = eld_id;
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLicense_plate_num() {
        return license_plate_num;
    }

    public void setLicense_plate_num(String license_plate_num) {
        this.license_plate_num = license_plate_num;
    }

    public String getLicense_plate_issue_state() {
        return license_plate_issue_state;
    }

    public void setLicense_plate_issue_state(String license_plate_issue_state) {
        this.license_plate_issue_state = license_plate_issue_state;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getNotes_vehicle() {
        return notes_vehicle;
    }

    public void setNotes_vehicle(String notes_vehicle) {
        this.notes_vehicle = notes_vehicle;
    }

    public String getEld_id() {
        return eld_id;
    }

    public void setEld_id(String eld_id) {
        this.eld_id = eld_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
