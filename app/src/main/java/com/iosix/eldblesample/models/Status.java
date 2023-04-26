package com.iosix.eldblesample.models;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.iosix.eldblesample.models.eld_records.Point;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "log_table")
public class Status {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "to_status")
    @SerializedName("status")
    String status;

    @ColumnInfo(name = "location")
    @SerializedName("point")
    String location;

    @ColumnInfo(name = "note")
    @SerializedName("note")
    String note;

    @ColumnInfo(name = "time")
    @SerializedName("cr_time")
    String time;

    public Status(String status,String location,String note,String time){
        this.status = status;
        this.location = location;
        this.note = note;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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
