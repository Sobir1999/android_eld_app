package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class CachedEngineRecord {

    @SerializedName("vin")
    private String vin;

    @SerializedName("odometer")
    private double odometer;

    @SerializedName("engine_hours")
    private double engine_hours;

    @SerializedName("time")
    private int time;

    @SerializedName("sequence_number")
    private int sequence_number;

    @SerializedName("type")
    private String type;

    public CachedEngineRecord(String vin, double odometer, double engine_hours, int time, int sequence_number, String type){
        this.vin = vin;
        this.odometer = odometer;
        this.engine_hours = engine_hours;
        this.time = time;
        this.sequence_number = sequence_number;
        this.type = type;
    }

    public String getVin() {
        return vin;
    }

    public double getOdometer() {
        return odometer;
    }

    public double getEngine_hours() {
        return engine_hours;
    }

    public int getTime() {
        return time;
    }

    public int getSequence_number() {
        return sequence_number;
    }

    public String getType() {
        return type;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public void setEngine_hours(double engine_hours) {
        this.engine_hours = engine_hours;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setSequence_number(int sequence_number) {
        this.sequence_number = sequence_number;
    }

    public void setType(String type) {
        this.type = type;
    }


}
