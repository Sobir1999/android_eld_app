package com.iosix.eldblesample.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ManageStatusDialog extends Dialog {

    private final Handler handler = new Handler();
    private Runnable runnable;
    private int counter = 60;
    private final EldJsonViewModel eldJsonViewModel;
    private final StatusDaoViewModel statusDaoViewModel;
    private final double latitude;
    private final double longtitude;
    private DateFormat format;
    private DriverSharedPrefs driverSharedPrefs;
    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];

    public ManageStatusDialog(@NonNull Context context, EldJsonViewModel eldJsonViewModel, StatusDaoViewModel statusDaoViewModel,double latitude,double longtitude) {
        super(context);
        this.eldJsonViewModel = eldJsonViewModel;
        this.statusDaoViewModel = statusDaoViewModel;
        this.latitude = latitude;
        this.longtitude = longtitude;
        driverSharedPrefs = DriverSharedPrefs.getInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_confirmation_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = findViewById(R.id.idConfirmationTitle);
        Button stay = findViewById(R.id.idDialogStayDriving);
        Button go = findViewById(R.id.idDialogChangeStatus);
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(longtitude);
        arrayList.add(latitude);

        format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        runnable = () -> {
            if (counter > 0){
                counter--;
                handler.postDelayed(runnable,1000L);
                textView.setText(counter + " sec");
            }else {
                handler.removeCallbacks(runnable);
                cancel();
//                eldJsonViewModel.postStatus(new Status("ON",new Point("Point",arrayList)
//                        ,"on", format.format(Calendar.getInstance().getTime())));

//                statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_ON,new Point("Point",arrayList),
//                        "on", null, today, getCurrentSeconds()));
            }
        };
        runnable.run();

        stay.setOnClickListener(view -> {
            cancel();
            handler.removeCallbacks(runnable);
        });

        go.setOnClickListener(view -> {
//            eldJsonViewModel.postStatus(new Status("OFF",new Point("Point",arrayList)
//                    ,"off", format.format(Calendar.getInstance().getTime())));

//            statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_OFF,new Point("Point",arrayList),
//                    "off", null, today, getCurrentSeconds()));

            cancel();
            handler.removeCallbacks(runnable);
        });

    }
}
