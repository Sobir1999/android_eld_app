package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.userData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.dialogs.CustomTimePickerDialog;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import java.util.Calendar;

public class NightModeActivity extends BaseActivity {

    private SwitchCompat idSwitchCompat;
    private ImageView imageView;
    private TextView idTvStart;
    private TextView idTvEnd;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
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
        imageView = findViewById(R.id.idImageBack);
        idTvStart = findViewById(R.id.idTvStart);
        idTvEnd = findViewById(R.id.idTvEnd);
        imageView.setOnClickListener(view -> onBackPressed());

        if (userData.getStartTime()>12){
            if ((userData.getStartTime()-12) > 9){
                idTvStart.setText((userData.getStartTime()-12) + ":" + "00" + " PM");
            }else {
                idTvStart.setText("0" + (userData.getStartTime()-12) + ":" + "00" + " PM");
            }
        }else {
            if (userData.getStartTime()>9){
                idTvStart.setText(userData.getStartTime() + ":" + "00" + " AM");
            }else {
                idTvStart.setText("0" + userData.getStartTime() + ":" + "00" + " AM");
            }
        }

        if (userData.getEndTime()>12){
            if ((userData.getEndTime()-12) > 9){
                idTvEnd.setText((userData.getEndTime()-12) + ":" + "00" + " PM");
            }else {
                idTvEnd.setText("0" + (userData.getEndTime()-12) + ":" + "00" + " PM");
            }
        }else {
            if (userData.getEndTime()>9){
                idTvEnd.setText(userData.getEndTime() + ":" + "00" + " AM");
            }else {
                idTvEnd.setText("0" + userData.getEndTime() + ":" + "00" + " AM");
            }
        }

        idTvStart.setOnClickListener(view -> {
            CustomTimePickerDialog pickerDialog = new CustomTimePickerDialog(this, 2, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    userData.saveStartTime(i);
                    if (i>12){
                        if ((i-12) > 9){
                            idTvStart.setText((i-12) + ":" + "00" + " PM");
                        }else {
                            idTvStart.setText("0" + (i-12) + ":" + "00" + " PM");
                        }
                    }else {
                        if (i>9){
                            idTvStart.setText(i + ":" + "00" + " AM");
                        }else {
                            idTvStart.setText("0" + i + ":" + "00" + " AM");
                        }
                    }
                }
            },hour,minute,false);
            pickerDialog.setTitle("Select Time");
            pickerDialog.show();
        });

        idTvEnd.setOnClickListener(view -> {
            CustomTimePickerDialog pickerDialog = new CustomTimePickerDialog(this, 2, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    userData.saveEndTime(i);
                    if (i>12){
                        if ((i-12) > 9){
                            idTvEnd.setText((i-12) + ":" + "00" + " PM");
                        }else {
                            idTvEnd.setText("0" + (i-12) + ":" + "00" + " PM");
                        }
                    }else {
                        if (i>9){
                            idTvEnd.setText(i + ":" + "00" + " AM");
                        }else {
                            idTvEnd.setText("0" + i + ":" + "00" + " AM");
                        }
                    }
                }
            },hour,minute,false);
            pickerDialog.setTitle("Select Time");
            pickerDialog.show();
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
}