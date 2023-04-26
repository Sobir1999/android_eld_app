package com.iosix.eldblesample.roomDatabase.entities;

import com.google.gson.annotations.SerializedName;
import com.iosix.eldblesample.models.eld_records.Point;

import java.util.ArrayList;

public class LiveDataEntitiy {

    @SerializedName("engine_state")
    private ArrayList<Boolean> engine_state;

    @SerializedName("vin")
    private ArrayList<String> vin;

    @SerializedName("speed")
    private ArrayList<Double> speed;

    @SerializedName("odometer")
    private ArrayList<Double> odometer;

    @SerializedName("trip_distance")
    private ArrayList<Double> trip_distance;

    @SerializedName("engine_hours")
    private ArrayList<Double> engine_hours;

    @SerializedName("trip_hours")
    private ArrayList<Double> trip_hours;

    @SerializedName("battery_voltage")
    private ArrayList<Double> battery_voltage;

    @SerializedName("moment")
    private ArrayList<String> date;

    @SerializedName("point")
    private ArrayList<String> point;

    @SerializedName("gps_speed")
    private ArrayList<Integer> gps_speed;

    @SerializedName("course")
    private ArrayList<Integer> course;

    @SerializedName("number_of_satellites")
    private ArrayList<Integer> number_of_satellites;

    @SerializedName("altitude")
    private ArrayList<Integer> altitude;

    @SerializedName("dop")
    private ArrayList<Double> dop;

    @SerializedName("sequence_number")
    private ArrayList<Integer> sequence_number;

    @SerializedName("firmware_version")
    private ArrayList<String> firmware_version;

    public LiveDataEntitiy(ArrayList<Boolean> engine_state, ArrayList<String> vin, ArrayList<Double> speed, ArrayList<Double> odometer, ArrayList<Double> trip_distance, ArrayList<Double> engine_hours, ArrayList<Double> trip_hours, ArrayList<Double> battery_voltage, ArrayList<String> date, ArrayList<String> point, ArrayList<Integer> gps_speed, ArrayList<Integer> course, ArrayList<Integer> number_of_satellites, ArrayList<Integer> altitude, ArrayList<Double> dop, ArrayList<Integer> sequence_number, ArrayList<String> firmware_version) {
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


    public ArrayList<Boolean> getEngine_state() {
        return engine_state;
    }

    public void setEngine_state(ArrayList<Boolean> engine_state) {
        this.engine_state = engine_state;
    }

    public ArrayList<String> getVin() {
        return vin;
    }

    public void setVin(ArrayList<String> vin) {
        this.vin = vin;
    }

    public ArrayList<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(ArrayList<Double> speed) {
        this.speed = speed;
    }

    public ArrayList<Double> getOdometer() {
        return odometer;
    }

    public void setOdometer(ArrayList<Double> odometer) {
        this.odometer = odometer;
    }

    public ArrayList<Double> getTrip_distance() {
        return trip_distance;
    }

    public void setTrip_distance(ArrayList<Double> trip_distance) {
        this.trip_distance = trip_distance;
    }

    public ArrayList<Double> getEngine_hours() {
        return engine_hours;
    }

    public void setEngine_hours(ArrayList<Double> engine_hours) {
        this.engine_hours = engine_hours;
    }

    public ArrayList<Double> getTrip_hours() {
        return trip_hours;
    }

    public void setTrip_hours(ArrayList<Double> trip_hours) {
        this.trip_hours = trip_hours;
    }

    public ArrayList<Double> getBattery_voltage() {
        return battery_voltage;
    }

    public void setBattery_voltage(ArrayList<Double> battery_voltage) {
        this.battery_voltage = battery_voltage;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public ArrayList<String> getPoint() {
        return point;
    }

    public void setPoint(ArrayList<String> point) {
        this.point = point;
    }

    public ArrayList<Integer> getGps_speed() {
        return gps_speed;
    }

    public void setGps_speed(ArrayList<Integer> gps_speed) {
        this.gps_speed = gps_speed;
    }

    public ArrayList<Integer> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<Integer> course) {
        this.course = course;
    }

    public ArrayList<Integer> getNumber_of_satellites() {
        return number_of_satellites;
    }

    public void setNumber_of_satellites(ArrayList<Integer> number_of_satellites) {
        this.number_of_satellites = number_of_satellites;
    }

    public ArrayList<Integer> getAltitude() {
        return altitude;
    }

    public void setAltitude(ArrayList<Integer> altitude) {
        this.altitude = altitude;
    }

    public ArrayList<Double> getDop() {
        return dop;
    }

    public void setDop(ArrayList<Double> dop) {
        this.dop = dop;
    }

    public ArrayList<Integer> getSequence_number() {
        return sequence_number;
    }

    public void setSequence_number(ArrayList<Integer> sequence_number) {
        this.sequence_number = sequence_number;
    }

    public ArrayList<String> getFirmware_version() {
        return firmware_version;
    }

    public void setFirmware_version(ArrayList<String> firmware_version) {
        this.firmware_version = firmware_version;
    }
}
