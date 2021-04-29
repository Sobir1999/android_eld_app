package com.iosix.eldblesample.roomDatabase.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "status_table")
public class TruckStatusEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "status")
    private String status = "off";
    @ColumnInfo(name = "location")
    @Nullable
    private String location;
    @ColumnInfo(name = "note")
    @Nullable
    private String note;
    @ColumnInfo(name = "document")
    @Nullable
    private String document;
    @ColumnInfo(name = "time")
    @NonNull
    private int time;
    @ColumnInfo(name = "day")
    @NonNull
    private String day;
    @ColumnInfo(name = "year")
    private int year;

    public TruckStatusEntity(String status, @Nullable String location, @Nullable String note, @Nullable String document, int time, @NonNull String day, int year) {
        this.id = id;
        this.status = status;
        this.location = location;
        this.note = note;
        this.document = document;
        this.time = time;
        this.day = day;
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    public void setLocation(@Nullable String location) {
        this.location = location;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    @Nullable
    public String getDocument() {
        return document;
    }

    public void setDocument(@Nullable String document) {
        this.document = document;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @NonNull
    public String getDay() {
        return day;
    }

    public void setDay(@NonNull String day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
