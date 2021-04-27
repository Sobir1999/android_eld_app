package com.iosix.eldblesample.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.iosix.eldblesample.MainActivity;

import java.io.File;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createLocaleFile();
        Log.d("TAG", "onCreate: oncreate APP");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void createLocaleFile() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            File myDir = new File(Environment.getExternalStorageDirectory(), "FastLogz");
            if (myDir.exists()) {
                Log.d("TAG", "createLocaleFile: Already Exist");
            } else {
                myDir.mkdirs();
                if (myDir.isDirectory()) {
                    Toast.makeText(this, "Directory created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

}
