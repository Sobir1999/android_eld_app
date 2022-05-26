package com.iosix.eldblesample.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.iosix.eldblesample.R;

import java.io.ByteArrayOutputStream;

public class SignaturePrefs {

    Context context;
    private static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;


    public SignaturePrefs(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE);
    }

    public void saveImageName(String name){
        editor = sharedPreferences.edit();
        editor.putString("image",name);
        editor.apply();
    }

    public String fetchImageName(){
        return sharedPreferences.getString("image", null);
    }


    public void saveSignature(Bitmap signature){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] compressImage = baos.toByteArray();
        String sEncodedImage = Base64.encodeToString(compressImage, Base64.DEFAULT);

        sharedPreferences.edit().putString("keyStoredSignature",sEncodedImage).apply();
    }

    public void saveMechanicSignature(Bitmap mechanicSignature){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mechanicSignature.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] compressImage = baos.toByteArray();
        String sEncodedImage = Base64.encodeToString(compressImage, Base64.DEFAULT);

        sharedPreferences.edit().putString("keyStoredMechanicSignature",sEncodedImage).apply();
    }

    public void saveDefect(boolean defect){
        sharedPreferences.edit().putBoolean("Defect",defect).apply();
    }

    public boolean fetchDefect(){
        return sharedPreferences.getBoolean("Defect",false);
    }

    public Bitmap fetchSignature(){

            String encodedImage = sharedPreferences.getString("keyStoredSignature",null);
            if (encodedImage != null){
                byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            }
        return null;
    }

    public Bitmap fetchMechanicSignature(){

        String encodedImage = sharedPreferences.getString("keyStoredMechanicSignature",null);
        if (encodedImage != null){
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    public void clearSignature(){
        sharedPreferences.edit().remove("keyStoredSignature").commit();
    }

    public void clearMechanicSignature(){
        sharedPreferences.edit().remove("keyStoredMechanicSignature").commit();
    }

    public void clearDefect(){
        sharedPreferences.edit().remove("Defect").commit();
    }
}
