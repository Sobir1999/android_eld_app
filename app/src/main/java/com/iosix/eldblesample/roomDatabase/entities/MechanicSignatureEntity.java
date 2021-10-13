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

    public MechanicSignatureEntity(Bitmap mechanicSignatureBitmap){
        this.mechanicSignatureBitmap = mechanicSignatureBitmap;
    }

    public void setMechanicSignatureBitmap(Bitmap mechanicSignatureBitmap){this.mechanicSignatureBitmap = mechanicSignatureBitmap;}

    public Bitmap getMechanicSignatureBitmap(){return mechanicSignatureBitmap;}

    public void setId(int id){this.id = id;}

    public int getId(){return id;}
}
