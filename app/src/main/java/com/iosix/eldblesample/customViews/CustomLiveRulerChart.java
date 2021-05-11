package com.iosix.eldblesample.customViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.iosix.eldblesample.enums.TableConstants;
import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomLiveRulerChart extends CustomRulerChart {
    private float START_POINT_X = TableConstants.START_POINT_X;
    private float START_POINT_Y = TableConstants.START_POINT_Y;
    private int currentDate;
    private int last_status;

    private ArrayList<TruckStatusEntity> arrayList = new ArrayList<TruckStatusEntity>() {{
//        add(new TruckStatusEntity(1, 2, 1000));
//        add(new TruckStatusEntity(2, 0, 2000));
//        add(new TruckStatusEntity(0, 1, 5000));
//        add(new TruckStatusEntity(1, 3, 15000));
//        add(new TruckStatusEntity(3, 1, 25000));
    }};

    private ArrayList<Paint> paintArray = new ArrayList<Paint>() {{
        add(TableConstants.getOFFPaint());
        add(TableConstants.getSBPaint());
        add(TableConstants.getDRPaint());
        add(TableConstants.getONPaint());
    }};

    public CustomLiveRulerChart(Context context) {
        super(context);
        init(context);
    }

    public CustomLiveRulerChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomLiveRulerChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        SharedPreferences pref = context
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        last_status = pref.getInt("last_P", 0);
    }

    @Override
    public void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 3;
        float startX = START_POINT_X;
        float startY = 0;
        float endX = 0;
        float endY = 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int hour = Calendar.getInstance().getTime().getHours();
                int minut = Calendar.getInstance().getTime().getMinutes();
                int second = Calendar.getInstance().getTime().getSeconds();
                currentDate = hour * 3600 + minut * 60 + second;
            }
        }, 500);

        for (int i = 0; i < arrayList.size(); i++) {
            startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getFrom_status()) / 4;
            endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
            endX = START_POINT_X + (arrayList.get(i).getSeconds() * 8) / CUSTOM_TABLE_WIDTH;
            canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getFrom_status()));
            canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getFrom_status()));
            startX = endX;
        }

        if (!arrayList.isEmpty()) {
            canvas.drawLine(endX, endY, START_POINT_X + (currentDate * 8) / CUSTOM_TABLE_WIDTH, endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));
        } else {
            endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT/8 + (CUSTOM_TABLE_HEIGHT*last_status)/4;
            canvas.drawLine(TableConstants.START_POINT_X, endY, START_POINT_X + (currentDate*8)/CUSTOM_TABLE_WIDTH, endY, paintArray.get(last_status));
        }

        invalidate();
    }

    public void mInvalidate() {
        invalidate();
    }
}
