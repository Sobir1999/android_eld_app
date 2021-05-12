package com.iosix.eldblesample.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.iosix.eldblesample.MainActivity;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.TableConstants;
import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

import java.util.ArrayList;

public class CustomStableRulerChart extends CustomRulerChart {
    private float START_POINT_X = TableConstants.START_POINT_X;
    private float START_POINT_Y = TableConstants.START_POINT_Y;
    private int last_status;
    private int off = 0, sb = 0, dr = 0, on = 0;

    private ArrayList<TruckStatusEntity> arrayList = new ArrayList();

    private ArrayList<Paint> paintArray = new ArrayList<Paint>() {{
        add(TableConstants.getOFFPaint());
        add(TableConstants.getSBPaint());
        add(TableConstants.getDRPaint());
        add(TableConstants.getONPaint());
    }};

    public CustomStableRulerChart(Context context) {
        super(context);
        init(context);
    }

    public CustomStableRulerChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomStableRulerChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        SharedPreferences pref = context
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        last_status = pref.getInt("last_P", 0);
    }

    public void setArrayList(ArrayList<TruckStatusEntity> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 3;
        float startX = START_POINT_X;
        float startY = 0;
        float endX = 0;
        float endY = 0;
        int off = 0, sb = 0, dr = 0, on = 0;

        int start = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getFrom_status()) / 4;
            endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
            endX = START_POINT_X + (arrayList.get(i).getSeconds() * 8) / CUSTOM_TABLE_WIDTH;
            canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getFrom_status()));
            canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getFrom_status()));
            startX = endX;

            if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_OFF) {
                off += (arrayList.get(i).getSeconds() - start);
            }
            if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_SB) {
                sb += (arrayList.get(i).getSeconds() - start);
            }
            if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_DR) {
                dr += (arrayList.get(i).getSeconds() - start);
            }
            if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_ON) {
                on += (arrayList.get(i).getSeconds() - start);
            }
            start = arrayList.get(i).getSeconds();
        }

        if (!arrayList.isEmpty()) {
            canvas.drawLine(endX, endY, START_POINT_X + (86399 * 8) / CUSTOM_TABLE_WIDTH, endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));

            if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_OFF) {
                off += (86400 - start);
            }
            if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_SB) {
                sb += (86400 - start);
            }
            if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_DR) {
                dr += (86400 - start);
            }
            if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_ON) {
                on += (86400 - start);
            }
        } else {
            endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * last_status) / 4;
            canvas.drawLine(TableConstants.START_POINT_X, endY, START_POINT_X + (86399 * 8) / CUSTOM_TABLE_WIDTH, endY, paintArray.get(last_status));

            if (last_status == EnumsConstants.STATUS_OFF) {
                off = 86400;
            }
            if (last_status == EnumsConstants.STATUS_SB) {
                sb = 86400;
            }
            if (last_status == EnumsConstants.STATUS_DR) {
                dr = 86400;
            }
            if (last_status == EnumsConstants.STATUS_ON) {
                on = 86400;
            }
        }

        this.off = off;
        this.sb = sb;
        this.dr = dr;
        this.on = on;
    }

    @Override
    public void drawTextTime(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 3;
        canvas.drawText(
                getTime(off),
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                TableConstants.getOFFPaint()
        );
        canvas.drawText(
                getTime(sb),
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8,
                TableConstants.getSBPaint()
        );
        canvas.drawText(
                getTime(dr),
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8,
                TableConstants.getDRPaint()
        );
        canvas.drawText(
                getTime(on),
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8,
                TableConstants.getONPaint()
        );

        invalidate();
    }

    @SuppressLint("DefaultLocale")
    private String getTime(int sum) {
        String s = "";
        int hour = sum / 3600;
        int min = (sum % 3600) / 60;
        s = String.format("%02d:%02d", hour, min);
        return s;
    }
}
