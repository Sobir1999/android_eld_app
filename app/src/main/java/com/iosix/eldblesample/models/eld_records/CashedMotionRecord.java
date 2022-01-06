package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CashedMotionRecord {

    @SerializedName("rpm")
    private double rpm;

    @SerializedName("speed")
    private double speed;

    @SerializedName("odometer")
    private double odometer;

    @SerializedName("engineHours")
    private double engineHours;

    @SerializedName("point")
    private Point point;

    @SerializedName("gps_speed")
    private int gps_speed;

    @SerializedName("type")
    private String type;

    public CashedMotionRecord(double rpm, double speed, double odometer, double engineHours, Point point, int gps_speed, String type) {
        this.rpm = rpm;
        this.speed = speed;
        this.odometer = odometer;
        this.engineHours = engineHours;
        this.point = point;
        this.gps_speed = gps_speed;
        this.type = type;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
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

    public double getEngineHours() {
        return engineHours;
    }

    public void setEngineHours(double engineHours) {
        this.engineHours = engineHours;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getGps_speed() {
        return gps_speed;
    }

    public void setGps_speed(int gps_speed) {
        this.gps_speed = gps_speed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
