package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    Driver driver;

    public Data(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
