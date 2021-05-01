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
    private int status = 0;
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
    private String time;

    public TruckStatusEntity(int status, @Nullable String location, @Nullable String note, @Nullable String document, @NonNull String time) {
        this.status = status;
        this.location = location;
        this.note = note;
        this.document = document;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }
}
