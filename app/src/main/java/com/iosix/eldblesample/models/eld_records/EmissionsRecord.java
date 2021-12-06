package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmissionsRecord {
    @SerializedName("nox_inlet")
    private double nox_inlet;

    @SerializedName("nox_outlet")
    private double nox_outlet;

    @SerializedName("ash_load")
    private double ash_load;

    @SerializedName("dpf_soot")
    private double dpf_soot;

    @SerializedName("dpf_regeneration")
    private int dpf_regeneration;

    @SerializedName("dpf_differential_pressure")
    private double dpf_differential_pressure;

    @SerializedName("egr_valve_position")
    private double egr_valve_position;

    @SerializedName("after_treatment_fuel_pressure")
    private double after_treatment_fuel_pressure;

    @SerializedName("engine_exhaust_temperature")
    private double engine_exhaust_temperature;

    @SerializedName("exhaust_temperature_1")
    private double exhaust_temperature_1;

    @SerializedName("exhaust_temperature_2")
    private double exhaust_temperature_2;

    @SerializedName("exhaust_temperature_3")
    private double exhaust_temperature_3;

    @SerializedName("def_level")
    private double def_level;

    @SerializedName("def_tank_temperature")
    private double def_tank_temperature;

    @SerializedName("scr_inducement_fault_state")
    private double scr_inducement_fault_state;

    public EmissionsRecord(double nox_inlet, double nox_outlet, double ash_load, double dpf_soot, int dpf_regeneration, double dpf_differential_pressure, double egr_valve_position, double after_treatment_fuel_pressure, double engine_exhaust_temperature, double exhaust_temperature_1, double exhaust_temperature_2, double exhaust_temperature_3, double def_level, double def_tank_temperature, double scr_inducement_fault_state) {
        this.nox_inlet = nox_inlet;
        this.nox_outlet = nox_outlet;
        this.ash_load = ash_load;
        this.dpf_soot = dpf_soot;
        this.dpf_regeneration = dpf_regeneration;
        this.dpf_differential_pressure = dpf_differential_pressure;
        this.egr_valve_position = egr_valve_position;
        this.after_treatment_fuel_pressure = after_treatment_fuel_pressure;
        this.engine_exhaust_temperature = engine_exhaust_temperature;
        this.exhaust_temperature_1 = exhaust_temperature_1;
        this.exhaust_temperature_2 = exhaust_temperature_2;
        this.exhaust_temperature_3 = exhaust_temperature_3;
        this.def_level = def_level;
        this.def_tank_temperature = def_tank_temperature;
        this.scr_inducement_fault_state = scr_inducement_fault_state;
    }

    public double getNox_inlet() {
        return nox_inlet;
    }

    public void setNox_inlet(double nox_inlet) {
        this.nox_inlet = nox_inlet;
    }

    public double getNox_outlet() {
        return nox_outlet;
    }

    public void setNox_outlet(double nox_outlet) {
        this.nox_outlet = nox_outlet;
    }

    public double getAsh_load() {
        return ash_load;
    }

    public void setAsh_load(double ash_load) {
        this.ash_load = ash_load;
    }

    public double getDpf_soot() {
        return dpf_soot;
    }

    public void setDpf_soot(double dpf_soot) {
        this.dpf_soot = dpf_soot;
    }

    public int getDpf_regeneration() {
        return dpf_regeneration;
    }

    public void setDpf_regeneration(int dpf_regeneration) {
        this.dpf_regeneration = dpf_regeneration;
    }

    public double getDpf_differential_pressure() {
        return dpf_differential_pressure;
    }

    public void setDpf_differential_pressure(double dpf_differential_pressure) {
        this.dpf_differential_pressure = dpf_differential_pressure;
    }

    public double getEgr_valve_position() {
        return egr_valve_position;
    }

    public void setEgr_valve_position(double egr_valve_position) {
        this.egr_valve_position = egr_valve_position;
    }

    public double getAfter_treatment_fuel_pressure() {
        return after_treatment_fuel_pressure;
    }

    public void setAfter_treatment_fuel_pressure(double after_treatment_fuel_pressure) {
        this.after_treatment_fuel_pressure = after_treatment_fuel_pressure;
    }

    public double getEngine_exhaust_temperature() {
        return engine_exhaust_temperature;
    }

    public void setEngine_exhaust_temperature(double engine_exhaust_temperature) {
        this.engine_exhaust_temperature = engine_exhaust_temperature;
    }

    public double getExhaust_temperature_1() {
        return exhaust_temperature_1;
    }

    public void setExhaust_temperature_1(double exhaust_temperature_1) {
        this.exhaust_temperature_1 = exhaust_temperature_1;
    }

    public double getExhaust_temperature_2() {
        return exhaust_temperature_2;
    }

    public void setExhaust_temperature_2(double exhaust_temperature_2) {
        this.exhaust_temperature_2 = exhaust_temperature_2;
    }

    public double getExhaust_temperature_3() {
        return exhaust_temperature_3;
    }

    public void setExhaust_temperature_3(double exhaust_temperature_3) {
        this.exhaust_temperature_3 = exhaust_temperature_3;
    }

    public double getDef_level() {
        return def_level;
    }

    public void setDef_level(double def_level) {
        this.def_level = def_level;
    }

    public double getDef_tank_temperature() {
        return def_tank_temperature;
    }

    public void setDef_tank_temperature(double def_tank_temperature) {
        this.def_tank_temperature = def_tank_temperature;
    }

    public double getScr_inducement_fault_state() {
        return scr_inducement_fault_state;
    }

    public void setScr_inducement_fault_state(double scr_inducement_fault_state) {
        this.scr_inducement_fault_state = scr_inducement_fault_state;
    }
}
