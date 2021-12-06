package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DriverBehaviorRecord {

    @SerializedName("cruise_control_speed")
    private double cruise_control_speed;

    @SerializedName("cruise_control_status")
    private int cruise_control_status;

    @SerializedName("throttle_position")
    private double throttle_position;

    @SerializedName("acceleration_position")
    private double acceleration_position;

    @SerializedName("brake")
    private double brake;

    @SerializedName("seat")
    private int seat;

    @SerializedName("steering_wheel")
    private double steering_wheel;

    @SerializedName("abc_status")
    private int abc_status;

    @SerializedName("traction_status")
    private double traction_status;

    @SerializedName("stability_status")
    private int stability_status;

    @SerializedName("break_system_pressure")
    private double break_system_pressure;

    public int getStability_status() {
        return stability_status;
    }

    public void setStability_status(int stability_status) {
        this.stability_status = stability_status;
    }

    public DriverBehaviorRecord(double cruise_control_speed, int cruise_control_status, double throttle_position, double acceleration_position, double brake, int seat, double steering_wheel, int abc_status, double traction_status, int stability_status, double break_system_pressure) {
        this.cruise_control_speed = cruise_control_speed;
        this.cruise_control_status = cruise_control_status;
        this.throttle_position = throttle_position;
        this.acceleration_position = acceleration_position;
        this.brake = brake;
        this.seat = seat;
        this.steering_wheel = steering_wheel;
        this.abc_status = abc_status;
        this.traction_status = traction_status;
        this.stability_status = stability_status;
        this.break_system_pressure = break_system_pressure;
    }

    public double getCruise_control_speed() {
        return cruise_control_speed;
    }

    public void setCruise_control_speed(double cruise_control_speed) {
        this.cruise_control_speed = cruise_control_speed;
    }

    public int getCruise_control_status() {
        return cruise_control_status;
    }

    public void setCruise_control_status(int cruise_control_status) {
        this.cruise_control_status = cruise_control_status;
    }

    public double getThrottle_position() {
        return throttle_position;
    }

    public void setThrottle_position(double throttle_position) {
        this.throttle_position = throttle_position;
    }

    public double getAcceleration_position() {
        return acceleration_position;
    }

    public void setAcceleration_position(double acceleration_position) {
        this.acceleration_position = acceleration_position;
    }

    public double getBrake() {
        return brake;
    }

    public void setBrake(double brake) {
        this.brake = brake;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getSteering_wheel() {
        return steering_wheel;
    }

    public void setSteering_wheel(double steering_wheel) {
        this.steering_wheel = steering_wheel;
    }

    public int getAbc_status() {
        return abc_status;
    }

    public void setAbc_status(int abc_status) {
        this.abc_status = abc_status;
    }

    public double getTraction_status() {
        return traction_status;
    }

    public void setTraction_status(double traction_status) {
        this.traction_status = traction_status;
    }

    public double getBreak_system_pressure() {
        return break_system_pressure;
    }

    public void setBreak_system_pressure(double break_system_pressure) {
        this.break_system_pressure = break_system_pressure;
    }
}
