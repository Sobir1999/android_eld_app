package com.iosix.eldblesample.roomDatabase.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Converter {

    @TypeConverter
  public Bitmap toBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    @TypeConverter
    public byte[] fromBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
        }
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static ZonedDateTime toDate(Long dateLong){
        return dateLong == null ? null: ZonedDateTime.ofInstant(Instant.ofEpochSecond(dateLong),ZoneId.systemDefault());
    }

    @TypeConverter
    public static Long fromDate(ZonedDateTime date){
        return date == null ? null : date.toInstant().getEpochSecond();
    }
}
