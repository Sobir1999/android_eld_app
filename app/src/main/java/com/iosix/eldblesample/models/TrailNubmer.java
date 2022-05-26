package com.iosix.eldblesample.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "trailer_table")
public class TrailNubmer {

    @SerializedName("trailer_number")
    @ColumnInfo(name = "trailer_id")
    String trail_number;

    public TrailNubmer(String trail_number) {
        this.trail_number = trail_number;
    }

    public String getTrail_number() {
        return trail_number;
    }

    public void setTrail_number(String trail_number) {
        this.trail_number = trail_number;
    }
}
