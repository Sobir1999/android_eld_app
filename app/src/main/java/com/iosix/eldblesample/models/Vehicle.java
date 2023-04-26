package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vehicle {

    @SerializedName("vehicles")
    List<VehicleList> vehicleList;

    public Vehicle(List<VehicleList> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public List<VehicleList> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<VehicleList> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
