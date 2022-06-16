package com.iosix.eldblesample.dialogs;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
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

    public ManageStatusDialog(@NonNull Context context, EldJsonViewModel eldJsonViewModel, StatusDaoViewModel statusDaoViewModel,double latitude,double longtitude) {
        super(context);
        this.eldJsonViewModel = eldJsonViewModel;
        this.statusDaoViewModel = statusDaoViewModel;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_confirmation_layout);
        TextView textView = findViewById(R.id.idConfirmationTitle);
        Button stay = findViewById(R.id.idDialogStayDriving);
        Button go = findViewById(R.id.idDialogChangeStatus);
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(latitude);
        arrayList.add(longtitude);

        format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        runnable = () -> {
            if (counter > 0){
                counter--;
                textView.setText(counter + " sec");
            }else {
                handler.removeCallbacks(runnable);
                cancel();
                eldJsonViewModel.postStatus(new Status("ON",new Point("Point",arrayList)
                        ,"on", format.format(Calendar.getInstance().getTime())));

                statusDaoViewModel.insertStatus(new LogEntity(EnumsConstants.STATUS_DR, EnumsConstants.STATUS_ON,null,
                        "on", null, format.format(Calendar.getInstance().getTime()), getCurrentSeconds()));
            }
            handler.postDelayed(runnable,1000L);
        };
        runnable.run();

        stay.setOnClickListener(view -> {
            cancel();
            handler.removeCallbacks(runnable);
        });

        go.setOnClickListener(view -> {
            cancel();
            handler.removeCallbacks(runnable);
            eldJsonViewModel.postStatus(new Status("OFF",new Point("Point",arrayList)
                    ,"off", format.format(Calendar.getInstance().getTime())));

            statusDaoViewModel.insertStatus(new LogEntity(EnumsConstants.STATUS_DR, EnumsConstants.STATUS_OFF,null,
                    "off", null, format.format(Calendar.getInstance().getTime()), getCurrentSeconds()));
        });
    }
}
