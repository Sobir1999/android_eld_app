package com.iosix.eldblesample.models;


import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Driver {

    @SerializedName("drivers")
    List<User> user;

    @SerializedName("available_vehicle_id")
    List<VehicleAvailable> vehicleAvailables;

    public List<VehicleAvailable> getVehicleAvailables() {
        return vehicleAvailables;
    }

    public void setVehicleAvailables(List<VehicleAvailable> vehicleAvailables) {
        this.vehicleAvailables = vehicleAvailables;
    }

    public Driver(List<User> user, List<VehicleAvailable> vehicleAvailables) {
        this.user = user;
        this.vehicleAvailables = vehicleAvailables;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }


}
