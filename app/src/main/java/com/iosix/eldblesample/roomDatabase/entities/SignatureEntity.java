package com.iosix.eldblesample.roomDatabase.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "signature_table")
public class SignatureEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "signature_id")
    private int id;

    @ColumnInfo(name = "signature")
    Bitmap signatureBitmap;

    public SignatureEntity(Bitmap signatureBitmap){
        this.signatureBitmap = signatureBitmap;
    }

    public void setSignatureBitmap(Bitmap signatureBitmap){this.signatureBitmap = signatureBitmap;}

    public Bitmap getSignatureBitmap(){return signatureBitmap;}

    public void setId(int id){this.id = id;}

    public int getId(){return id;}
}
