package com.iosix.eldblesample.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.SignatureFragment;
import com.iosix.eldblesample.models.SendDvir;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.shared_prefs.UserData;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION.SDK_INT;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignatureActivity extends BaseActivity {

    private ArrayList<String> mParams;
    private ArrayList<String> selectedTrailers;
    private String day;
    private DvirViewModel dvirViewModel;
    private SignatureViewModel signatureViewModel;
    private SignaturePrefs signaturePrefs;
    private APIInterface apiInterface;
    private SessionManager sessionManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signature;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();
        ImageView img = findViewById(R.id.idImageBack);
        TextView save = findViewById(R.id.idAddDvirSave);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        boolean mParam1 = getIntent().getBooleanExtra("isTruckSelected", false);
        mParams = getIntent().getStringArrayListExtra("arrayList");
        selectedTrailers = getIntent().getStringArrayListExtra("selectedTrailers");
        day = getIntent().getStringExtra("day");
        signaturePrefs = new SignaturePrefs(this);
        sessionManager = new SessionManager(this);

        apiInterface = ApiClient.getClient().create(APIInterface.class);

        verifyStoragePermissions(this);

        img.setOnClickListener(v -> onBackPressed());

        FragmentManager fragmentManager = getSupportFragmentManager();
        SignatureFragmentAdapter adapter = new SignatureFragmentAdapter(fragmentManager, getLifecycle(), mParam1,mParams,day);
        viewPager.setAdapter(adapter);

        signatureViewModel = new SignatureViewModel(this.getApplication());
        signatureViewModel = ViewModelProviders.of(this).get(SignatureViewModel.class);
        signatureViewModel.getMgetAllSignatures().observe(this, signatureEntities -> {
        });

        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        dvirViewModel.getMgetDvirs().observe(this, dvirEntities -> {
        });

        save.setOnClickListener(v -> {
            if(signaturePrefs.fetchDefect()){
                if (signaturePrefs.fetchSignature() == null){
                    if (signaturePrefs.fetchMechanicSignature() == null){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("Signature missed")
                                .setMessage("Mechanic must sign")
                                .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("Signature missed")
                                .setMessage("Sign or take your saved signatures")
                                .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                }else{
                    if (signaturePrefs.fetchMechanicSignature() == null){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("Signature missed")
                                .setMessage("Mechanic must sign")
                                .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                        AlertDialog alert = alertDialog.create();
                        alert.show();

                    }else {
                        SignatureEntity signatureEntity = new SignatureEntity(signaturePrefs.fetchSignature());
                        try {
                            signatureViewModel.insertSignature(signatureEntity);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        MechanicSignatureEntity mechanicSignatureEntity = new MechanicSignatureEntity(signaturePrefs.fetchMechanicSignature());
                        try {
                            signatureViewModel.insertMechanicSignature(mechanicSignatureEntity);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        saveTempBitmap(signaturePrefs.fetchSignature());
                        saveTempBitmap(signaturePrefs.fetchMechanicSignature());
                        String currDay;
                        currDay = mParams.get(6);
                        try {
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),getString(selectedTrailers),mParams.get(1),mParams.get(2),true,
                                    mParams.get(3),mParams.get(4),mParams.get(5), day
                            ));
                            Intent intent = new Intent(SignatureActivity.this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("currDay",currDay);
                            startActivity(intent);

                            sessionManager.saveSignature(signaturePrefs.fetchSignature());

                            apiInterface.sendDvir(new SendDvir(null,null,null,mParams.get(3),
                                    mParams.get(4),mParams.get(5))).enqueue(new Callback<SendDvir>() {
                                @Override
                                public void onResponse(Call<SendDvir> call, Response<SendDvir> response) {
                                    try {
                                        Log.d("Adverse Diving",response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (response.isSuccessful()){
                                        Log.d("Adverse Diving","Response is Successfull");
                                    }
                                }

                                @Override
                                public void onFailure(Call<SendDvir> call, Throwable t) {
                                    Log.d("Adverse Diving",t.getMessage());
                                }
                            });

                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                if (signaturePrefs.fetchSignature() == null){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Signature missed")
                            .setMessage("Sign or take your saved signatures")
                            .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }else{
                    SignatureEntity signatureEntity = new SignatureEntity(signaturePrefs.fetchSignature());
                    try {
                        signatureViewModel.insertSignature(signatureEntity);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    saveTempBitmap(signaturePrefs.fetchSignature());
                    String currDay;
                    currDay = mParams.get(6);
                    try {
                        if (!mParams.get(1).equals("No unit selected") || !mParams.get(2).equals("No trailer selected")){
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),getString(selectedTrailers),mParams.get(1),mParams.get(2),false,
                                    mParams.get(3),mParams.get(4),mParams.get(5), day
                            ));
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("currDay",currDay);
                            startActivity(intent);
                            sessionManager.saveSignature(signaturePrefs.fetchSignature());

                            apiInterface.sendDvir(new SendDvir(null,null,null,mParams.get(3),
                                    mParams.get(4),mParams.get(5))).enqueue(new Callback<SendDvir>() {
                                @Override
                                public void onResponse(Call<SendDvir> call, Response<SendDvir> response) {
                                    try {
                                        Log.d("Adverse Diving",response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (response.isSuccessful()){
                                        Log.d("Adverse Diving","Response is Successfull");
                                    }
                                }

                                @Override
                                public void onFailure(Call<SendDvir> call, Throwable t) {
                                    Log.d("Adverse Diving",t.getMessage());
                                }
                            });
                        }else {
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),getString(selectedTrailers),mParams.get(1),mParams.get(2),true,
                                    mParams.get(3),mParams.get(4),mParams.get(5), day
                            ));
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("currDay",currDay);
                            startActivity(intent);

                            sessionManager.saveSignature(signaturePrefs.fetchSignature());


                            apiInterface.sendDvir(new SendDvir(null,null,null,mParams.get(3),
                                    mParams.get(4),mParams.get(5))).enqueue(new Callback<SendDvir>() {
                                @Override
                                public void onResponse(Call<SendDvir> call, Response<SendDvir> response) {
                                    try {
                                        Log.d("Adverse Diving",response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (response.isSuccessful()){
                                        Log.d("Adverse Diving","Response is Successfull");
                                    }
                                }

                                @Override
                                public void onFailure(Call<SendDvir> call, Throwable t) {
                                    Log.d("Adverse Diving",t.getMessage());
                                }
                            });

                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void saveTempBitmap(Bitmap bitmap) {
            saveImage(bitmap);
    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/FastLogz" + "/images");
        //noinspection ResultOfMethodCallIgnored
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Signature_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            UserData userData = new UserData(appContext);
            if (userData.getMode()){
                finalBitmap.eraseColor(Color.BLACK);
            }else {
                finalBitmap.eraseColor(Color.WHITE);
            }
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
//    public boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//    }

    public static void verifyStoragePermissions(Activity activity) {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);//permission request code is just an int
            }
        }else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private String getString(ArrayList<String> arrayList){
        if (arrayList.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append(arrayList.get(i)).append(",");
            }
            return stringBuilder.toString();
        }else return "";
    }
}

class SignatureFragmentAdapter extends FragmentStateAdapter {

    private final boolean mParam1;
    private final ArrayList<String> mParams;
    private final String day;

    public SignatureFragmentAdapter(@NonNull FragmentManager fragmentManager,
                                    @NonNull Lifecycle lifecycle,boolean mParam1,
                                    ArrayList<String> mParams,String day) {
        super(fragmentManager, lifecycle);
        this.mParam1 = mParam1;
        this.mParams = mParams;
        this.day = day;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
            return SignatureFragment.newInstance(mParam1,mParams,day);
        }

    @Override
    public int getItemCount() {
        return 1;
    }
}