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

    @ColumnInfo(name = "signature")
    Bitmap signatureBitmap;

    @ColumnInfo(name = "day")
    String day;

    public SignatureEntity(Bitmap signatureBitmap,String day){
        this.signatureBitmap = signatureBitmap;
        this.day = day;
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
