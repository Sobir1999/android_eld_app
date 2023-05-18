package com.iosix.eldblesample.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.dialogs.CustomTimePickerDialog;
import com.iosix.eldblesample.fragments.TimePickerFragment;
import com.iosix.eldblesample.shared_prefs.UserData;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NightModeActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener{

    private SwitchCompat idSwitchCompat;
    private TextView idTvStart;
    private TextView idTvEnd;
    private UserData userData;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);

    private String timeString = "";
    private int n = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_night_mode;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();
        idSwitchCompat = findViewById(R.id.idSwitchCompat);
        ImageView imageView = findViewById(R.id.idImageBack);
        idTvStart = findViewById(R.id.idTvStart);
        idTvEnd = findViewById(R.id.idTvEnd);
        imageView.setOnClickListener(view -> onBackPressed());
        userData = new UserData(this);

        if (userData.getStartTime() > 12){
            idTvStart.setText(String.format("%s:%s %s", timeString(userData.getStartTime()-12), timeString(userData.getStartMin()),"PM"));
        }else {
            idTvStart.setText(String.format("%s:%s %s", timeString(userData.getStartTime()), timeString(userData.getStartMin()),"AM"));
        }

        if (userData.getEndTime() > 12){
            idTvStart.setText(String.format("%s:%s %s", timeString(userData.getEndTime()-12), timeString(userData.getEndMin()),"PM"));
        }else {
            idTvStart.setText(String.format("%s:%s %s", timeString(userData.getEndTime()), timeString(userData.getEndMin()),"AM"));
        }

        idTvStart.setOnClickListener(view -> {
            n=1;
            DialogFragment timeFragment = new TimePickerFragment(this);
            timeFragment.show(getSupportFragmentManager(), "time picker");
        });

        idTvEnd.setOnClickListener(view -> {
            n=2;
            DialogFragment timeFragment = new TimePickerFragment(this);
            timeFragment.show(getSupportFragmentManager(), "time picker");
        });

        getModeState();
    }

    private void getModeState(){
        checkNightModeActivated();
        idSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                idSwitchCompat.setChecked(true);
                userData.saveAutoSwitch(true);
                recreate();
            }else {
                recreate();
                idSwitchCompat.setChecked(false);
                userData.saveAutoSwitch(false);
            }
        });
    }

    private void checkNightModeActivated() {
        idSwitchCompat.setChecked(userData.getAutoSwitch());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Time time = new Time(calendar.getTimeInMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSSSSS", Locale.US);
        timeString = format.format(time);
        if (n==1){
            userData.saveStartTime(hourOfDay);
            userData.saveStartMin(minute);
            if (hourOfDay > 12){
                idTvStart.setText(String.format("%s:%s %s", timeString(hourOfDay-12), timeString(minute),"PM"));
            }else {
                idTvStart.setText(String.format("%s:%s %s", timeString(hourOfDay), timeString(minute),"AM"));
            }
        }else if (n ==2){
            userData.saveEndTime(hourOfDay);
            userData.saveEndMin(minute);
            if (hourOfDay > 12){
                idTvEnd.setText(String.format("%s:%s %s", timeString(hourOfDay-12), timeString(minute),"PM"));
            }else {
                idTvEnd.setText(String.format("%s:%s %s", timeString(hourOfDay), timeString(minute),"AM"));
            }
        }

    }

    private String timeString(int digit) {
        String s = "" + digit;
        if (s.length() == 1) s = "0" + s;
        return s;
    }
}