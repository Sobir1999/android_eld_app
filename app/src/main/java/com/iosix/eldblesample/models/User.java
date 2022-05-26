package com.iosix.eldblesample.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.iosix.eldblesample.roomDatabase.converter.TrailerConverter;
import com.iosix.eldblesample.roomDatabase.converter.TrailerConverterString;

import java.util.ArrayList;

import retrofit2.http.Query;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    private String lastName;

    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    private String phone;

    @SerializedName("driver_license_number")
    @ColumnInfo(name = "driver_license_number")
    private String driverId;

    @SerializedName("co_driver")
    @ColumnInfo(name = "co_driver")
    private String coDriver;

    @SerializedName("home_terminal_address")
    @ColumnInfo(name = "home_terminal_address")
    private String homeTerminalAddress;

    @SerializedName("home_terminal_time_zone")
    @ColumnInfo(name = "home_terminal_time_zone")
    private String timeZone;

    @SerializedName("trail_number")
    @TypeConverters(TrailerConverterString.class)
    @ColumnInfo(name = "trail_number")
    private ArrayList<String> trailNumber;

    @SerializedName("notes_driver")
    @ColumnInfo(name = "notes_driver")
    private String notes;

    @SerializedName("vehicle_id")
    @ColumnInfo(name = "vehicle_id")
    private String vehicleId;

    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String image;

    @SerializedName("main_office")
    @ColumnInfo(name = "main_office")
    private String mainOffice;

    @SerializedName("company")
    @ColumnInfo(name = "company")
    private String company;

    public User(String name, String lastName, String phone, String driverId, String coDriver, String homeTerminalAddress,
                String timeZone, ArrayList<String> trailNumber, String notes, String vehicleId, String image, String mainOffice,String company) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.driverId = driverId;
        this.coDriver = coDriver;
        this.homeTerminalAddress = homeTerminalAddress;
        this.timeZone = timeZone;
        this.trailNumber = trailNumber;
        this.notes = notes;
        this.vehicleId = vehicleId;
        this.image = image;
        this.mainOffice = mainOffice;
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCoDriver() {
        return coDriver;
    }

    public void setCoDriver(String coDriver) {
        this.coDriver = coDriver;
    }

    public String getHomeTerminalAddress() {
        return homeTerminalAddress;
    }

    public void setHomeTerminalAddress(String homeTerminalAddress) {
        this.homeTerminalAddress = homeTerminalAddress;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public ArrayList<String> getTrailNumber() {
        return trailNumber;
    }

    public void setTrailNumber(ArrayList<String> trailNumber) {
        this.trailNumber = trailNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMainOffice() {
        return mainOffice;
    }

    public void setMainOffice(String mainOffice) {
        this.mainOffice = mainOffice;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
