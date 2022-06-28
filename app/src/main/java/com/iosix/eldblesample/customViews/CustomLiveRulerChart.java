package com.iosix.eldblesample.customViews;

import static com.iosix.eldblesample.MyApplication.userData;
import static com.iosix.eldblesample.activity.RecapActivity.drTime;
import static com.iosix.eldblesample.activity.RecapActivity.offTime;
import static com.iosix.eldblesample.activity.RecapActivity.onTime;
import static com.iosix.eldblesample.activity.RecapActivity.sbTime;
import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.TableConstants;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.ArrayList;

public class CustomLiveRulerChart extends CustomRulerChart {
    private final float START_POINT_X = TableConstants.START_POINT_X;
    private final float START_POINT_Y = TableConstants.START_POINT_Y;
    private int off = 0, sb = 0, dr = 0, on = 0;

    private ArrayList<LogEntity> arrayList = new ArrayList();

    private final ArrayList<Paint> paintArray = new ArrayList<Paint>() {{
        add(TableConstants.getOFFPaint(getContext()));
        add(TableConstants.getSBPaint(getContext()));
        add(TableConstants.getDRPaint(getContext()));
        add(TableConstants.getONPaint(getContext()));
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
    }

    public void setArrayList(ArrayList<LogEntity> arrayList) {
        this.arrayList = arrayList;
        invalidate();
    }

    @Override
    public void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        float startX = START_POINT_X + CUSTOM_TABLE_WIDTH / 26f;
        float startPointxX = START_POINT_X + CUSTOM_TABLE_WIDTH / 26f;
        float startY;
        float endX = 0;
        float endY = 0;

        int off = 0, sb = 0, dr = 0, on = 0;

        int start = 0;


        for (int i = 0; i < arrayList.size(); i++) {
            startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getFrom_status()) / 4;
            endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
            endX = startPointxX + (arrayList.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/26);
            canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getFrom_status()));
            canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getFrom_status()));
            startX = endX;

            if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_OFF) {
                off += (arrayList.get(i).getSeconds() - start);
            }
            else if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_SB) {
                sb += (arrayList.get(i).getSeconds() - start);
            }
            else if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_DR) {
                dr += (arrayList.get(i).getSeconds() - start);
            }
            else if (arrayList.get(i).getFrom_status() == EnumsConstants.STATUS_ON) {
                on += (arrayList.get(i).getSeconds() - start);
            }
            start = arrayList.get(i).getSeconds();
        }

        if (!arrayList.isEmpty()) {

            canvas.drawLine(endX, endY, startPointxX + ((getCurrentSeconds() / (3600*24f)) * CUSTOM_TABLE_WIDTH*24/26), endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));

            if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_OFF) {
                off += (getCurrentSeconds() - start);
            }
            else if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_SB) {
                sb += (getCurrentSeconds() - start);
            }
            else if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_DR) {
                dr += (getCurrentSeconds() - start);
            }
            else if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_ON) {
                on += (getCurrentSeconds() - start);
            }
        }

        this.off = off;
        this.sb = sb;
        this.dr = dr;
        this.on = on;

        invalidate();
    }

    @Override
    public void drawTextTime(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        canvas.drawText(
                getTime(off),
                START_POINT_X + 201*CUSTOM_TABLE_WIDTH/208,
                START_POINT_Y + 5*CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getOFFPaint(getContext())
        );
        canvas.drawText(
                getTime(sb),
                START_POINT_X + 201*CUSTOM_TABLE_WIDTH/208,
                START_POINT_Y + 13 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getSBPaint(getContext())
        );
        canvas.drawText(
                getTime(dr),
                START_POINT_X + 201*CUSTOM_TABLE_WIDTH/208,
                START_POINT_Y + 21 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getDRPaint(getContext())
        );
        canvas.drawText(
                getTime(on),
                START_POINT_X + 201*CUSTOM_TABLE_WIDTH/208,
                START_POINT_Y + 29 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getONPaint(getContext())
        );

        invalidate();
    }

    @SuppressLint("DefaultLocale")
    private String getTime(int sum) {
        String s;
        int hour = sum / 3600;
        int min = (sum % 3600) / 60;
        s = String.format("%02d:%02d", hour, min);
        return s;
    }

    @SuppressLint("DefaultLocale")
    public String getDr() {
        String s;
        int hour = dr / 3600;
        int min = (dr % 3600) / 60;
        s = String.format("%02dh %02dm", hour, min);
        return s;
    }

}
