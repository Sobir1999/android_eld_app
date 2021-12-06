package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class EngineLiveRecord {

    @SerializedName("oil_pressure")
    private double oil_pressure;

    @SerializedName("turbo_boost")
    private double turbo_boost;

    @SerializedName("intake_pressure")
    private double intake_pressure;

    @SerializedName("fuel_pressure")
    private double fuel_pressure;

    @SerializedName("load")
    private double load;

    @SerializedName("mass_air_flow")
    private double mass_air_flow;

    @SerializedName("turbo_rpm")
    private double turbo_rpm;

    @SerializedName("intake_temperature")
    private double intake_temperature;

    @SerializedName("coolant_temperature")
    private double coolant_temperature;

    @SerializedName("oil_temperature")
    private double oil_temperature;

    @SerializedName("fuel_temperature")
    private double fuel_temperature;

    @SerializedName("change_cooler_temperature")
    private double change_cooler_temperature;

    @SerializedName("torque")
    private double torque;

    @SerializedName("oil_level")
    private double oil_level;

    @SerializedName("coolant_level")
    private double coolant_level;

    @SerializedName("trip_fuel")
    private double trip_fuel;

    @SerializedName("fuel_economy")
    private double fuel_economy;

    public EngineLiveRecord(double oil_pressure, double turbo_boost, double intake_pressure, double fuel_pressure, double load, double mass_air_flow, double turbo_rpm, double intake_temperature, double coolant_temperature, double oil_temperature, double fuel_temperature, double change_cooler_temperature, double torque, double oil_level, double coolant_level, double trip_fuel, double fuel_economy) {
        this.oil_pressure = oil_pressure;
        this.turbo_boost = turbo_boost;
        this.intake_pressure = intake_pressure;
        this.fuel_pressure = fuel_pressure;
        this.load = load;
        this.mass_air_flow = mass_air_flow;
        this.turbo_rpm = turbo_rpm;
        this.intake_temperature = intake_temperature;
        this.coolant_temperature = coolant_temperature;
        this.oil_temperature = oil_temperature;
        this.fuel_temperature = fuel_temperature;
        this.change_cooler_temperature = change_cooler_temperature;
        this.torque = torque;
        this.oil_level = oil_level;
        this.coolant_level = coolant_level;
        this.trip_fuel = trip_fuel;
        this.fuel_economy = fuel_economy;
    }

    public double getOil_pressure() {
        return oil_pressure;
    }

    public void setOil_pressure(double oil_pressure) {
        this.oil_pressure = oil_pressure;
    }

    public double getTurbo_boost() {
        return turbo_boost;
    }

    public void setTurbo_boost(double turbo_boost) {
        this.turbo_boost = turbo_boost;
    }

    public double getIntake_pressure() {
        return intake_pressure;
    }

    public void setIntake_pressure(double intake_pressure) {
        this.intake_pressure = intake_pressure;
    }

    public double getFuel_pressure() {
        return fuel_pressure;
    }

    public void setFuel_pressure(double fuel_pressure) {
        this.fuel_pressure = fuel_pressure;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }

    public double getMass_air_flow() {
        return mass_air_flow;
    }

    public void setMass_air_flow(double mass_air_flow) {
        this.mass_air_flow = mass_air_flow;
    }

    public double getTurbo_rpm() {
        return turbo_rpm;
    }

    public void setTurbo_rpm(double turbo_rpm) {
        this.turbo_rpm = turbo_rpm;
    }

    public double getIntake_temperature() {
        return intake_temperature;
    }

    public void setIntake_temperature(double intake_temperature) {
        this.intake_temperature = intake_temperature;
    }

    public double getCoolant_temperature() {
        return coolant_temperature;
    }

    public void setCoolant_temperature(double coolant_temperature) {
        this.coolant_temperature = coolant_temperature;
    }

    public double getOil_temperature() {
        return oil_temperature;
    }

    public void setOil_temperature(double oil_temperature) {
        this.oil_temperature = oil_temperature;
    }

    public double getFuel_temperature() {
        return fuel_temperature;
    }

    public void setFuel_temperature(double fuel_temperature) {
        this.fuel_temperature = fuel_temperature;
    }

    public double getChange_cooler_temperature() {
        return change_cooler_temperature;
    }

    public void setChange_cooler_temperature(double change_cooler_temperature) {
        this.change_cooler_temperature = change_cooler_temperature;
    }

    public double getTorque() {
        return torque;
    }

    public void setTorque(double torque) {
        this.torque = torque;
    }

    public double getOil_level() {
        return oil_level;
    }

    public void setOil_level(double oil_level) {
        this.oil_level = oil_level;
    }

    public double getCoolant_level() {
        return coolant_level;
    }

    public void setCoolant_level(double coolant_level) {
        this.coolant_level = coolant_level;
    }

    public double getTrip_fuel() {
        return trip_fuel;
    }

    public void setTrip_fuel(double trip_fuel) {
        this.trip_fuel = trip_fuel;
    }

    public double getFuel_economy() {
        return fuel_economy;
    }

    public void setFuel_economy(double fuel_economy) {
        this.fuel_economy = fuel_economy;
    }
}
