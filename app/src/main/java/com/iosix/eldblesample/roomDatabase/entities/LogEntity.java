package com.iosix.eldblesample.roomDatabase.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "log_table")
public class LogEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "from_status")
    private int from_status = 0;
    @ColumnInfo(name = "to_status")
    private int to_status = 0;
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
    private String time;
    @ColumnInfo(name = "seconds")
    private int seconds;

    public LogEntity(int from_status, int to_status, @Nullable String location, @Nullable String note, @Nullable String document, @NonNull String time, int seconds) {
        this.from_status = from_status;
        this.to_status = to_status;
        this.location = location;
        this.note = note;
        this.document = document;
        this.time = time;
        this.seconds = seconds;
    }

    @Ignore
    public LogEntity(int from_status, int to_status, int seconds) {
        this.from_status = from_status;
        this.to_status = to_status;
        this.seconds = seconds;
    }

    public int getFrom_status() {
        return from_status;
    }

    public void setFrom_status(int from_status) {
        this.from_status = from_status;
    }

    public int getTo_status() {
        return to_status;
    }

    public void setTo_status(int to_status) {
        this.to_status = to_status;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
