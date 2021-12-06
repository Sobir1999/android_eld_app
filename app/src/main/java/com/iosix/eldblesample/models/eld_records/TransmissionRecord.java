package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class TransmissionRecord {

    @SerializedName("output_shaft_rpm")
    private double output_shaft_rpm;

    @SerializedName("gear")
    private int gear;

    @SerializedName("request_gear_status")
    private int request_gear_status;

    @SerializedName("transmission_oil_temperature")
    private double transmission_oil_temperature;

    @SerializedName("torque_converter_lockup_status")
    private int torque_converter_lockup_status;

    public TransmissionRecord(double output_shaft_rpm, int gear, int request_gear_status, double transmission_oil_temperature, int torque_converter_lockup_status) {
        this.output_shaft_rpm = output_shaft_rpm;
        this.gear = gear;
        this.request_gear_status = request_gear_status;
        this.transmission_oil_temperature = transmission_oil_temperature;
        this.torque_converter_lockup_status = torque_converter_lockup_status;
    }

    public double getOutput_shaft_rpm() {
        return output_shaft_rpm;
    }

    public void setOutput_shaft_rpm(double output_shaft_rpm) {
        this.output_shaft_rpm = output_shaft_rpm;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public int getRequest_gear_status() {
        return request_gear_status;
    }

    public void setRequest_gear_status(int request_gear_status) {
        this.request_gear_status = request_gear_status;
    }

    public double getTransmission_oil_temperature() {
        return transmission_oil_temperature;
    }

    public void setTransmission_oil_temperature(double transmission_oil_temperature) {
        this.transmission_oil_temperature = transmission_oil_temperature;
    }

    public int getTorque_converter_lockup_status() {
        return torque_converter_lockup_status;
    }

    public void setTorque_converter_lockup_status(int torque_converter_lockup_status) {
        this.torque_converter_lockup_status = torque_converter_lockup_status;
    }
}
