package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class FuelRecord {

    @SerializedName("fuel_level")
    private double fuel_level;

    @SerializedName("integrated_fuel")
    private double integrated_fuel;

    @SerializedName("total_fuel_consumed")
    private double total_fuel_consumed;

    @SerializedName("fuel_rate")
    private double fuel_rate;

    @SerializedName("idle_fuel_consumed")
    private double idle_fuel_consumed;

    @SerializedName("idle_time")
    private double idle_time;

    @SerializedName("high_rpm_state")
    private int high_rpm_state;

    @SerializedName("unsteady_state")
    private int unsteady_state;

    @SerializedName("engine_power_state")
    private int engine_power_state;

    @SerializedName("accel_state")
    private int accel_state;

    @SerializedName("eco")
    private int eco;

    @SerializedName("anticipate_state")
    private int anticipate_state;

    public FuelRecord(double fuel_level, double integrated_fuel, double total_fuel_consumed, double fuel_rate, double idle_fuel_consumed, double idle_time, int high_rpm_state, int unsteady_state, int engine_power_state, int accel_state, int eco, int anticipate_state) {
        this.fuel_level = fuel_level;
        this.integrated_fuel = integrated_fuel;
        this.total_fuel_consumed = total_fuel_consumed;
        this.fuel_rate = fuel_rate;
        this.idle_fuel_consumed = idle_fuel_consumed;
        this.idle_time = idle_time;
        this.high_rpm_state = high_rpm_state;
        this.unsteady_state = unsteady_state;
        this.engine_power_state = engine_power_state;
        this.accel_state = accel_state;
        this.eco = eco;
        this.anticipate_state = anticipate_state;
    }

    public double getFuel_level() {
        return fuel_level;
    }

    public void setFuel_level(double fuel_level) {
        this.fuel_level = fuel_level;
    }

    public double getIntegrated_fuel() {
        return integrated_fuel;
    }

    public void setIntegrated_fuel(double integrated_fuel) {
        this.integrated_fuel = integrated_fuel;
    }

    public double getTotal_fuel_consumed() {
        return total_fuel_consumed;
    }

    public void setTotal_fuel_consumed(double total_fuel_consumed) {
        this.total_fuel_consumed = total_fuel_consumed;
    }

    public double getFuel_rate() {
        return fuel_rate;
    }

    public void setFuel_rate(double fuel_rate) {
        this.fuel_rate = fuel_rate;
    }

    public double getIdle_fuel_consumed() {
        return idle_fuel_consumed;
    }

    public void setIdle_fuel_consumed(double idle_fuel_consumed) {
        this.idle_fuel_consumed = idle_fuel_consumed;
    }

    public double getIdle_time() {
        return idle_time;
    }

    public void setIdle_time(double idle_time) {
        this.idle_time = idle_time;
    }

    public int getHigh_rpm_state() {
        return high_rpm_state;
    }

    public void setHigh_rpm_state(int high_rpm_state) {
        this.high_rpm_state = high_rpm_state;
    }

    public int getUnsteady_state() {
        return unsteady_state;
    }

    public void setUnsteady_state(int unsteady_state) {
        this.unsteady_state = unsteady_state;
    }

    public int getEngine_power_state() {
        return engine_power_state;
    }

    public void setEngine_power_state(int engine_power_state) {
        this.engine_power_state = engine_power_state;
    }

    public int getAccel_state() {
        return accel_state;
    }

    public void setAccel_state(int accel_state) {
        this.accel_state = accel_state;
    }

    public int getEco() {
        return eco;
    }

    public void setEco(int eco) {
        this.eco = eco;
    }

    public int getAnticipate_state() {
        return anticipate_state;
    }

    public void setAnticipate_state(int anticipate_state) {
        this.anticipate_state = anticipate_state;
    }
}

