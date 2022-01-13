package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

public class VehicleAvailable {

    @SerializedName("1")
    String vehicleAvailable;

    public String getVehicleAvailable() {
        return vehicleAvailable;
    }

    public void setVehicleAvailable(String vehicleAvailable) {
        this.vehicleAvailable = vehicleAvailable;
    }

    public VehicleAvailable(String vehicleAvailable) {
        this.vehicleAvailable = vehicleAvailable;
    }


}
