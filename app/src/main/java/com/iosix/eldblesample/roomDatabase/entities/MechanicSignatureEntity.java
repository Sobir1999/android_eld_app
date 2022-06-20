package com.iosix.eldblesample.roomDatabase.entities;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mechanic_signature_table")
public class MechanicSignatureEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "mechanicSignature")
    Bitmap mechanicSignatureBitmap;

    @ColumnInfo(name = "day")
    String day;

    public MechanicSignatureEntity(Bitmap mechanicSignatureBitmap,String day){
        this.mechanicSignatureBitmap = mechanicSignatureBitmap;
        this.day = day;
    }

    public void setMechanicSignatureBitmap(Bitmap mechanicSignatureBitmap){this.mechanicSignatureBitmap = mechanicSignatureBitmap;}

    public Bitmap getMechanicSignatureBitmap(){return mechanicSignatureBitmap;}

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
