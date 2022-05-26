package com.iosix.eldblesample.roomDatabase.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_id")
public class TrailerId {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "trailer_id")
    private Integer trailer_id;

    public TrailerId(Integer trailer_id) {
        this.trailer_id = trailer_id;
    }

    public Integer getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(@NonNull Integer trailer_id) {
        this.trailer_id = trailer_id;
    }
}
