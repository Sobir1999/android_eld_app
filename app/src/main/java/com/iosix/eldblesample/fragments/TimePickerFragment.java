package com.iosix.eldblesample.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.AddDvirActivity;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment{

    private TimePickerDialog.OnTimeSetListener mListener;
    private Activity mActivity;

    public TimePickerFragment(Activity context){
        mActivity = context;
        mListener = (TimePickerDialog.OnTimeSetListener) context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(mActivity,2,mListener,hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
}
