package com.iosix.eldblesample.models;

import com.iosix.eldblelib.EldEngineStates;

import java.util.Date;

public class SendExampleModelData {
    private EldEngineStates engine_state;
    private String vin;
    private double rpm;
    private double speed_kmh;
    private double trip_distance_km;
    private double hours;
    private double trip_hours;
    private double voltage;
    private String date;
    private Date time;
    private double latitude;
    private double longitude;
    private int gps_speed_kmh;
    private int course_deg;
    private int namsats;
    private int altitude;
    private double dop;
    private int sequence;
    private String firmware;


    public SendExampleModelData(EldEngineStates engine_state, String vin, double rpm, double speed_kmh, double trip_distance_km, double hours, double trip_hours, double voltage, String date, Date time, double latitude, double longitude, int gps_speed_kmh, int course_deg, int namsats, int altitude, double dop, int sequence, String firmware) {
        this.engine_state = engine_state;
        this.vin = vin;
        this.rpm = rpm;
        this.speed_kmh = speed_kmh;
        this.trip_distance_km = trip_distance_km;
        this.hours = hours;
        this.trip_hours = trip_hours;
        this.voltage = voltage;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gps_speed_kmh = gps_speed_kmh;
        this.course_deg = course_deg;
        this.namsats = namsats;
        this.altitude = altitude;
        this.dop = dop;
        this.sequence = sequence;
        this.firmware = firmware;
    }

    public EldEngineStates getEngine_state() {
        return engine_state;
    }

    public void setEngine_state(EldEngineStates engine_state) {
        this.engine_state = engine_state;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public double getSpeed_kmh() {
        return speed_kmh;
    }

    public void setSpeed_kmh(double speed_kmh) {
        this.speed_kmh = speed_kmh;
    }

    public double getTrip_distance_km() {
        return trip_distance_km;
    }

    public void setTrip_distance_km(double trip_distance_km) {
        this.trip_distance_km = trip_distance_km;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getTrip_hours() {
        return trip_hours;
    }

    public void setTrip_hours(double trip_hours) {
        this.trip_hours = trip_hours;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getGps_speed_kmh() {
        return gps_speed_kmh;
    }

    public void setGps_speed_kmh(int gps_speed_kmh) {
        this.gps_speed_kmh = gps_speed_kmh;
    }

    public int getCourse_deg() {
        return course_deg;
    }

    public void setCourse_deg(int course_deg) {
        this.course_deg = course_deg;
    }

    public int getNamsats() {
        return namsats;
    }

    public void setNamsats(int namsats) {
        this.namsats = namsats;
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }
}
