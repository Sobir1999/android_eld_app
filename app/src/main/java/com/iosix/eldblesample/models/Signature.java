package com.iosix.eldblesample.models;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Signature {

    @SerializedName("sign")
    @ColumnInfo(name = "signature")
    String signatureBitmap;

    @SerializedName("created_date")
    Date day;

    public Signature(String signatureBitmap,Date day){
        this.signatureBitmap = signatureBitmap;
        this.day = day;
    }

    public void setSignatureBitmap(String signatureBitmap){this.signatureBitmap = signatureBitmap;}

    public String getSignatureBitmap(){return signatureBitmap;}


    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
}
