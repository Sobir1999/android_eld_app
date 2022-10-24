package com.iosix.eldblesample.roomDatabase.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "signature_table")
public class SignatureEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "signature_id")
    private int id;

    @ColumnInfo(name = "driver_id")
    private String driverId;

    @ColumnInfo(name = "signature")
    Bitmap signatureBitmap;

    @ColumnInfo(name = "day")
    String day;

    public SignatureEntity(String driverId,Bitmap signatureBitmap,String day){
        this.signatureBitmap = signatureBitmap;
        this.day = day;
        this.driverId = driverId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setSignatureBitmap(Bitmap signatureBitmap){this.signatureBitmap = signatureBitmap;}

    public Bitmap getSignatureBitmap(){return signatureBitmap;}

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
