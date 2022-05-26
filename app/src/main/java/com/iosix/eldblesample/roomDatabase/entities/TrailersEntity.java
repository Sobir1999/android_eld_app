package com.iosix.eldblesample.roomDatabase.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "trailers")
public class TrailersEntity {

    @PrimaryKey()
    @SerializedName("id")
    @ColumnInfo(name = "trailer_id")
    @NonNull
    private String trailer_id;

    @SerializedName("trailer_number")
    @ColumnInfo(name = "number")
    private String number;

    public TrailersEntity(String trailer_id,String number) {
        this.number = number;
        this.trailer_id = trailer_id;
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
