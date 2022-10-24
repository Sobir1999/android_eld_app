package com.iosix.eldblesample.roomDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "dvir_table", indices = {@Index(value = {"day"}, unique = true)})
public class DvirEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "driver_id")
    private String driverId;
    @ColumnInfo(name = "unit")
    private String unit;
    @ColumnInfo(name = "trailer")
    private String trailer;
    @ColumnInfo(name = "unitDefect")
    private String unitDefect;
    @ColumnInfo(name = "trailerDefect")
    private String trailerDefect;
    @ColumnInfo(name = "hasMechanicSignature")
    private boolean hasMechanicSignature;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "day")
    private String day;

    public DvirEntity(String driverId,String unit,String trailer,String unitDefect,String trailerDefect,boolean hasMechanicSignature, String time, String location, String note, String day) {
        this.unit = unit;
        this.trailer = trailer;
        this.unitDefect = unitDefect;
        this.trailerDefect = trailerDefect;
        this.hasMechanicSignature = hasMechanicSignature;
        this.time = time;
        this.location = location;
        this.note = note;
        this.day = day;
        this.driverId = driverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getUnitDefect() {
        return unitDefect;
    }

    public void setUnitDefect(String unitDefect) {
        this.unitDefect = unitDefect;
    }

    public String getTrailerDefect() {
        return trailerDefect;
    }

    public void setTrailerDefect(String trailerDefect) {
        this.trailerDefect = trailerDefect;
    }

    public boolean getHasMechanicSignature() {
        return hasMechanicSignature;
    }

    public void setHasMechanicSignature(boolean hasMechanicSignature) {
        this.hasMechanicSignature = hasMechanicSignature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
