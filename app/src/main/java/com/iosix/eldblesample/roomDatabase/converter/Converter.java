package com.iosix.eldblesample.roomDatabase.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class Converter {

    @TypeConverter
  public Bitmap toBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    @TypeConverter
    public byte[] fromBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
        return outputStream.toByteArray();
    }
}
