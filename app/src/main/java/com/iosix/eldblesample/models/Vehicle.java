package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Vehicle {

    @SerializedName("vehicles")
    List<VehicleList> vehicleList;

    @SerializedName("available_eld_id")
    List<VehicleAvailable> available_eld_id;

    public Vehicle(List<VehicleList> vehicleList, List<VehicleAvailable> available_eld_id) {
        this.vehicleList = vehicleList;
        this.available_eld_id = available_eld_id;
    }

    public List<VehicleList> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<VehicleList> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public List<VehicleAvailable> getAvailable_eld_id() {
        return available_eld_id;
    }

    public void setAvailable_eld_id(List<VehicleAvailable> available_eld_id) {
        this.available_eld_id = available_eld_id;
    }
}
