package com.iosix.eldblesample.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.TableConstants;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.ArrayList;

public class CustomStableRulerChart extends CustomRulerChart {
    private float START_POINT_X = TableConstants.START_POINT_X;
    private float START_POINT_Y = TableConstants.START_POINT_Y;
    private int last_status;
    private int off = 0, sb = 0, dr = 0, on = 0;

    private ArrayList<LogEntity> arrayList = new ArrayList();

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

    public void setArrayList(ArrayList<LogEntity> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        float startX = START_POINT_X+CUSTOM_TABLE_WIDTH / 26;
        float startPointX = START_POINT_X+CUSTOM_TABLE_WIDTH / 26;
        float startY = 0;
        float endX = 0;
        float endY = 0;
        int off = 0, sb = 0, dr = 0, on = 0;

        int start = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getFrom_status()) / 4;
            endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
            endX = startPointX + (arrayList.get(i).getSeconds() /(3600*24f)) * CUSTOM_TABLE_WIDTH*24/26;
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
            canvas.drawLine(endX, endY, startPointX + CUSTOM_TABLE_WIDTH*24/26, endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));

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
            endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * EnumsConstants.STATUS_OFF) / 4;
            canvas.drawLine(startX, endY, startPointX + CUSTOM_TABLE_WIDTH*24/26, endY, paintArray.get(EnumsConstants.STATUS_OFF));

                off = 86400;
        }

        this.off = off;
        this.sb = sb;
        this.dr = dr;
        this.on = on;
    }

    @Override
    public void drawTextTime(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        canvas.drawText(
                getTime(off),
                START_POINT_X + 25*CUSTOM_TABLE_WIDTH/26+2f,
                START_POINT_Y + 5*CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getOFFPaint()
        );
        canvas.drawText(
                getTime(sb),
                START_POINT_X + 25*CUSTOM_TABLE_WIDTH/26 +2f,
                START_POINT_Y + 13 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getSBPaint()
        );
        canvas.drawText(
                getTime(dr),
                START_POINT_X + 25*CUSTOM_TABLE_WIDTH/26 +2f,
                START_POINT_Y + 21 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getDRPaint()
        );
        canvas.drawText(
                getTime(on),
                START_POINT_X + 25*CUSTOM_TABLE_WIDTH/26 +2f,
                START_POINT_Y + 29 * CUSTOM_TABLE_HEIGHT / 32,
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
