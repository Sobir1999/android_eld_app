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
import android.os.ResultReceiver;
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
import com.iosix.eldblesample.interfaces.Communicator;
import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.shared_prefs.UserData;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Build.VERSION.SDK_INT;

import static com.iosix.eldblesample.enums.Day.stringToDay;
import static com.iosix.eldblesample.utils.Utils.defects;

public class SignatureActivity extends BaseActivity implements Communicator {

    private ArrayList<String> selectedTrailers;
    private ArrayList<String> unitDefects;
    private ArrayList<String> trailerDefects;
    private String day;
    private String notes;
    private String time;
    private String location;
    private String vehicle;
    private DvirViewModel dvirViewModel;
    private SignatureViewModel signatureViewModel;
    private SignaturePrefs signaturePrefs;
    private EldJsonViewModel eldJsonViewModel;
    private ResultReceiver resultReceiver;
    private Bitmap bitmap,mechanicBitmap;
    private boolean isChecked;
    private DriverSharedPrefs driverSharedPrefs;

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

        selectedTrailers = getIntent().getStringArrayListExtra("selectedTrailers");
        unitDefects = getIntent().getStringArrayListExtra("unitDefects");
        trailerDefects = getIntent().getStringArrayListExtra("trailerDefects");
        resultReceiver = getIntent().getParcelableExtra("finisher");
        day = getIntent().getStringExtra("day");
        notes = getIntent().getStringExtra("notes");
        time = getIntent().getStringExtra("time");
        location = getIntent().getStringExtra("location");
        vehicle = getIntent().getStringExtra("unit");
        unitDefects.addAll(trailerDefects);

        boolean mParam1 = unitDefects.size() > 0;
        signaturePrefs = new SignaturePrefs(this);

        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);

        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());

        verifyStoragePermissions(this);

        img.setOnClickListener(v -> onBackPressed());

        FragmentManager fragmentManager = getSupportFragmentManager();
        SignatureFragmentAdapter adapter = new SignatureFragmentAdapter(fragmentManager, getLifecycle(), mParam1,day);
        viewPager.setAdapter(adapter);

        signatureViewModel = new SignatureViewModel(this.getApplication());
        signatureViewModel = ViewModelProviders.of(this).get(SignatureViewModel.class);

        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);

        save.setOnClickListener(v -> {
            if (bitmap == null){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Signature missed")
                        .setMessage("Sign or take your saved signatures")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            } else {
                signatureViewModel.insertSignature(new SignatureEntity(bitmap,stringToDay(day)));

                Intent intent = new Intent(SignatureActivity.this, LGDDActivity.class);
                intent.putExtra("position", 3);
                intent.putExtra("currDay",day);
                if (!mParam1 || isChecked){
                    intent.putExtra("isSatisfactory",true);
                }else {
                    intent.putExtra("isSatisfactory",false);
                }

                startActivity(intent);
                finish();
                resultReceiver.send(2,new Bundle());

                eldJsonViewModel.sendDvir(new Dvir((!mParam1 || isChecked),vehicle,selectedTrailers,unitDefects,notes,time,location,stringToDay(day)));
                dvirViewModel.insertDvir(new Dvir((!mParam1 || isChecked),vehicle,selectedTrailers,unitDefects,notes,time,location,stringToDay(day)));
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

    @Override
    public void sendBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void sendMechanicBitmap(Bitmap bitmap) {
        this.mechanicBitmap = bitmap;
    }

    @Override
    public void sendHasDefect(Boolean b) {
        isChecked = b;
    }
}

class SignatureFragmentAdapter extends FragmentStateAdapter {

    private final boolean mParam1;
    private final String day;

    public SignatureFragmentAdapter(@NonNull FragmentManager fragmentManager,
                                    @NonNull Lifecycle lifecycle,boolean mParam1, String day) {
        super(fragmentManager, lifecycle);
        this.mParam1 = mParam1;
        this.day = day;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
            return SignatureFragment.newInstance(mParam1,day);
        }

    @Override
    public int getItemCount() {
        return 1;
    }

}