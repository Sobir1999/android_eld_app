package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

public class VehicleData {

    @SerializedName("data")
    Vehicle vehicle;

    public VehicleData(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
