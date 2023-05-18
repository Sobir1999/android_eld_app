package com.iosix.eldblesample.models.eld_records;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "local_data_table")
public class LiveDataRecord {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @SerializedName("engine_state")
    @ColumnInfo(name = "engine_state")
    Boolean engine_state;

    @SerializedName("vin")
    @ColumnInfo(name = "vin")
    private String vin;

    @SerializedName("speed")
    @ColumnInfo(name = "speed")
    private double speed;

    @SerializedName("odometer")
    @ColumnInfo(name = "odometer")
    private double odometer;

    @SerializedName("trip_distance")
    @ColumnInfo(name = "trip_distance")
    private double trip_distance;

    @SerializedName("engine_hours")
    @ColumnInfo(name = "engine_hours")
    private double engine_hours;

    @SerializedName("trip_hours")
    @ColumnInfo(name = "trip_hours")
    private double trip_hours;

    @SerializedName("battery_voltage")
    @ColumnInfo(name = "battery_voltage")
    private double battery_voltage;

    @SerializedName("created_date")
    @ColumnInfo(name = "moment")
    private String date;

    @SerializedName("point")
    @ColumnInfo(name = "point")
    private Point point;

    @SerializedName("gps_speed")
    @ColumnInfo(name = "gps_speed")
    private int gps_speed;

    @SerializedName("course")
    @ColumnInfo(name = "course")
    private int course;

    @SerializedName("number_of_satellites")
    @ColumnInfo(name = "number_of_satellites")
    private int number_of_satellites;

    @SerializedName("altitude")
    @ColumnInfo(name = "altitude")
    private int altitude;

    @SerializedName("dop")
    @ColumnInfo(name = "dop")
    private double dop;

    @SerializedName("sequence_number")
    @ColumnInfo(name = "sequence_number")
    private int sequence_number;

    @SerializedName("firmware_version")
    @ColumnInfo(name = "firmware_version")
    private String firmware_version;

    public LiveDataRecord(Boolean engine_state, String vin, double speed, double odometer, double trip_distance, double engine_hours, double trip_hours, double battery_voltage, String date, Point point, int gps_speed, int course, int number_of_satellites, int altitude, double dop, int sequence_number, String firmware_version) {
        this.engine_state = engine_state;
        this.vin = vin;
        this.speed = speed;
        this.odometer = odometer;
        this.trip_distance = trip_distance;
        this.engine_hours = engine_hours;
        this.trip_hours = trip_hours;
        this.battery_voltage = battery_voltage;
        this.date = date;
        this.point = point;
        this.gps_speed = gps_speed;
        this.course = course;
        this.number_of_satellites = number_of_satellites;
        this.altitude = altitude;
        this.dop = dop;
        this.sequence_number = sequence_number;
        this.firmware_version = firmware_version;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getEngine_state() {
        return engine_state;
    }

    public void setEngine_state(Boolean engine_state) {
        this.engine_state = engine_state;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public double getTrip_distance() {
        return trip_distance;
    }

    public void setTrip_distance(double trip_distance) {
        this.trip_distance = trip_distance;
    }

    public double getEngine_hours() {
        return engine_hours;
    }

    public void setEngine_hours(double engine_hours) {
        this.engine_hours = engine_hours;
    }

    public double getTrip_hours() {
        return trip_hours;
    }

    public void setTrip_hours(double trip_hours) {
        this.trip_hours = trip_hours;
    }

    public double getBattery_voltage() {
        return battery_voltage;
    }

    public void setBattery_voltage(double battery_voltage) {
        this.battery_voltage = battery_voltage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point   point) {
        this.point = point;
    }

    public int getGps_speed() {
        return gps_speed;
    }

    public void setGps_speed(int gps_speed) {
        this.gps_speed = gps_speed;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getNumber_of_satellites() {
        return number_of_satellites;
    }

    public void setNumber_of_satellites(int number_of_satellites) {
        this.number_of_satellites = number_of_satellites;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public double getDop() {
        return dop;
    }

    public void setDop(double dop) {
        this.dop = dop;
    }

    public int getSequence_number() {
        return sequence_number;
    }

    public void setSequence_number(int sequence_number) {
        this.sequence_number = sequence_number;
    }

    public String getFirmware_version() {
        return firmware_version;
    }

    public void setFirmware_version(String firmware_version) {
        this.firmware_version = firmware_version;
    }
}
