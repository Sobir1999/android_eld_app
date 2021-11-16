package com.iosix.eldblesample.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.SignatureFragment;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.shared_prefs.UserData;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION.SDK_INT;

public class SignatureActivity extends BaseActivity {

    private ImageView img;
    private TextView save;
    private boolean mParam1;
    private ArrayList<String> mParams;
    private String day;
    private ViewPager2 viewPager;
    private DvirViewModel dvirViewModel;
    private SignatureViewModel signatureViewModel;
    private DayDaoViewModel daoViewModel;
    private List<SignatureEntity> signatureEntities = new ArrayList<>();
    private List<DvirEntity> dvirEntities = new ArrayList<>();
    private SignaturePrefs signaturePrefs;

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
//        getWindow().setStatusBarColor(ActivityCompat.getColor(this,R.color.colorPrimaryDark));
        img = findViewById(R.id.idImageBack);
        save = findViewById(R.id.idAddDvirSave);
        viewPager = findViewById(R.id.viewpager);

        mParam1 = getIntent().getBooleanExtra("isTruckSelected",false);
        mParams = getIntent().getStringArrayListExtra("arrayList");
        day = getIntent().getStringExtra("day");
        signaturePrefs = new SignaturePrefs(this);
        daoViewModel = new DayDaoViewModel(this.getApplication());
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        Log.d("day","day:" + mParams.get(7));
        Log.d("day","day: " + day);

        verifyStoragePermissions(this);

        img.setOnClickListener(v -> onBackPressed());

        FragmentManager fragmentManager = getSupportFragmentManager();
        SignatureFragmentAdapter adapter = new SignatureFragmentAdapter(fragmentManager, getLifecycle(),mParam1,mParams,day);
        viewPager.setAdapter(adapter);

        signatureViewModel = new SignatureViewModel(this.getApplication());
        signatureViewModel = ViewModelProviders.of(this).get(SignatureViewModel.class);
        signatureViewModel.getMgetAllSignatures().observe(this, signatureEntities -> {
            this.signatureEntities = signatureEntities;
        });

        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        dvirViewModel.getMgetDvirs().observe(this, dvirEntities -> {
            this.dvirEntities = dvirEntities;
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
                        currDay = mParams.get(7);
                        try {
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),mParams.get(1),mParams.get(2),mParams.get(3),true,
                                    mParams.get(4),mParams.get(5),mParams.get(6), day
                            ));
                            Intent intent = new Intent(SignatureActivity.this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("day",currDay);
                            startActivity(intent);

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
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
                    currDay = mParams.get(7);
                    try {
                        if (!mParams.get(2).equals("No unit selected") || !mParams.get(3).equals("No trailer selected")){
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),mParams.get(1),mParams.get(2),mParams.get(3),false,
                                    mParams.get(4),mParams.get(5),mParams.get(6), day
                            ));
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("day",currDay);
                            startActivity(intent);
//                            loadFragment(LGDDFragment.newInstance(3,daoViewModel,currDay));
                        }else {
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),mParams.get(1),mParams.get(2),mParams.get(3),true,
                                    mParams.get(4),mParams.get(5),mParams.get(6), day
                            ));
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("day",currDay);
                            startActivity(intent);
//                            loadFragment(LGDDFragment.newInstance(3,daoViewModel,currDay));

                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
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
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Signature_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
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
            Log.d("myz", ""+SDK_INT);
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

    private void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.idFragmentSignatureContainer, fragment);
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.commit();
    }
}

class SignatureFragmentAdapter extends FragmentStateAdapter {

    private boolean mParam1;
    private ArrayList<String> mParams;
    private String day;

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