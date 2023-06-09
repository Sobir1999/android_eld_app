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
    private static volatile SignaturePrefs instance;
    private static final Object LOCK = new Object();


    public static SignaturePrefs getInstance(Context applicationContext){
        if (instance == null){
            synchronized (LOCK){
                instance = new SignaturePrefs(applicationContext);
            }
        }
        return instance;
    }

    public SignaturePrefs(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE);
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

}
