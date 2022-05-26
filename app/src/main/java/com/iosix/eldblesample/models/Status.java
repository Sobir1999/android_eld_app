package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;
import com.iosix.eldblesample.models.eld_records.Point;

public class Status {

    @SerializedName("status")
    String status;

    @SerializedName("point")
    Point location;

    @SerializedName("note")
    String note;

    @SerializedName("cr_time")
    String time;

    public Status(String status,Point location,String note,String time){
        this.status = status;
        this.location = location;
        this.note = note;
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
