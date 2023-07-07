package com.iosix.eldblesample.customViews;

import static com.iosix.eldblesample.enums.Day.getCurrentMillis;
import static com.iosix.eldblesample.enums.Day.stringToDate;
import static com.iosix.eldblesample.utils.Utils.getStatus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.TableConstants;
import com.iosix.eldblesample.models.Status;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomLiveRulerChart extends CustomRulerChart {
    private final float START_POINT_X = TableConstants.START_POINT_X;
    private final float START_POINT_Y = TableConstants.START_POINT_Y;
    private int off = 0, sb = 0, dr = 0, on = 0;
    float startX;
    float startPointxX;
    float startY;
    float endX;
    float endY;
    float CUSTOM_TABLE_HEIGHT;
    int start = 0;

    private List<Status> arrayList = new ArrayList<>();
    Status firstStatus = new Status(EnumsConstants.STATUS_OFF,null,null,"");

    private final ArrayList<Paint> paintArray = new ArrayList<Paint>() {{
        add(TableConstants.getOFFPaint(getContext()));
        add(TableConstants.getSBPaint(getContext()));
        add(TableConstants.getDRPaint(getContext()));
        add(TableConstants.getONPaint(getContext()));
        add(TableConstants.getOFFPCPaint(getContext()));
        add(TableConstants.getONYMPaint(getContext()));
        add(TableConstants.getPowerUpPaint(getContext()));
        add(TableConstants.getPowerDownPaint(getContext()));
        add(TableConstants.getLoginPaint(getContext()));
        add(TableConstants.getLogoutPaint(getContext()));
        add(TableConstants.getCertifiedPaint(getContext()));
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

    public void setArrayList(List<Status> arrayList,Status firstStatus) {
        this.arrayList = arrayList;
        this.firstStatus = firstStatus;
        invalidate();
    }

    @Override
    public void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        CUSTOM_TABLE_HEIGHT = 0;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 6;
        }else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        }
        startX = START_POINT_X + CUSTOM_TABLE_WIDTH / 14f;
        startPointxX = START_POINT_X + CUSTOM_TABLE_WIDTH / 14f;
        startY = 0;
        endX = startPointxX;
        endY = 0;
        start = 0;
        int off = 0, sb = 0, dr = 0, on = 0;

        for (int i = -1; i < arrayList.size() - 1; i++) {

            if (i == -1){
                drawChart(firstStatus,arrayList.get(0),canvas,CUSTOM_TABLE_WIDTH);

                if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_OFF) || Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_OF_PC)) {
                    off += (stringToDate(arrayList.get(0).getTime()).toLocalTime().toSecondOfDay()- start);
                }
                else if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_SB)) {
                    sb += (stringToDate(arrayList.get(0).getTime()).toLocalTime().toSecondOfDay() - start);
                }
                else if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_DR)) {
                    dr += (stringToDate(arrayList.get(0).getTime()).toLocalTime().toSecondOfDay() - start);
                }
                else if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_ON) || Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_ON_YM)) {
                    on += (stringToDate(arrayList.get(0).getTime()).toLocalTime().toSecondOfDay() - start);
                }
                start = stringToDate(arrayList.get(0).getTime()).toLocalTime().toSecondOfDay();
            }else {
                drawChart(arrayList.get(i),arrayList.get(i+1),canvas,CUSTOM_TABLE_WIDTH);

                if (Objects.equals(arrayList.get(i).getStatus(), EnumsConstants.STATUS_OFF) || Objects.equals(arrayList.get(i).getStatus(), EnumsConstants.STATUS_OF_PC)) {
                    off += (stringToDate(arrayList.get(i+1).getTime()).toLocalTime().toSecondOfDay()- start);
                }
                else if (Objects.equals(arrayList.get(i).getStatus(), EnumsConstants.STATUS_SB)) {
                    sb += (stringToDate(arrayList.get(i+1).getTime()).toLocalTime().toSecondOfDay() - start);
                }
                else if (Objects.equals(arrayList.get(i).getStatus(), EnumsConstants.STATUS_DR)) {
                    dr += (stringToDate(arrayList.get(i+1).getTime()).toLocalTime().toSecondOfDay() - start);
                }
                else if (Objects.equals(arrayList.get(i).getStatus(), EnumsConstants.STATUS_ON) || Objects.equals(arrayList.get(i).getStatus(), EnumsConstants.STATUS_ON_YM)) {
                    on += (stringToDate(arrayList.get(i+1).getTime()).toLocalTime().toSecondOfDay() - start);
                }
                start = stringToDate(arrayList.get(i+1).getTime()).toLocalTime().toSecondOfDay();
            }
        }

//            for (int i = 0; i < arrayList1.size(); i++) {
//                if (Objects.equals(arrayList1.get(i).getStatus(), EnumsConstants.LOGIN)){
//                    canvas.drawLine(startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis() * 60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(getStatus(arrayList1.get(i).getStatus())));
//                    Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_login_circle_svgrepo_com,null);
//                    icon.setBounds(Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()* 60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
//                    icon.setTint(Color.parseColor("#1B5E20"));
//                    icon.draw(canvas);
//                }else if (Objects.equals(arrayList1.get(i).getStatus(), EnumsConstants.LOGOUT)){
//                    canvas.drawLine(startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(getStatus(arrayList1.get(i).getStatus())));
//                    Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_circle_logout_svgrepo_com,null);
//                    icon.setBounds(Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
//                    icon.setTint(Color.parseColor("#C2185B"));
//                    icon.draw(canvas);
//                }else if (Objects.equals(arrayList1.get(i).getStatus(), EnumsConstants.CERTIFIED)){
//                    canvas.drawLine(startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(getStatus(arrayList1.get(i).getStatus())));
//                    Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_check_circle_8,null);
//                    icon.setBounds(Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
//                    icon.setTint(Color.parseColor("#00C853"));
//                    icon.draw(canvas);
//                }else if (Objects.equals(arrayList1.get(i).getStatus(), EnumsConstants.POWER_UP)){
//                    canvas.drawLine(startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(getStatus(arrayList1.get(i).getStatus())));
//                    Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_power_sign_in_a_circle_svgrepo_com,null);
//                    icon.setBounds(Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
//                    icon.setTint(Color.parseColor("#33691E"));
//                    icon.draw(canvas);
//                }else if (Objects.equals(arrayList1.get(i).getStatus(), EnumsConstants.POWER_DOWN)){
//                    canvas.drawLine(startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(getStatus(arrayList1.get(i).getStatus())));
//                    Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_power_sign_in_a_circle_svgrepo_com,null);
//                    icon.setBounds(Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointxX + (stringToDate(arrayList1.get(i).getTime()).getTimeInMillis()*60 / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
//                    icon.setTint(Color.parseColor("#D32F2F"));
//                    icon.draw(canvas);
//                }
//            }
//

        if (!arrayList.isEmpty()) {

            if (Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_OF_PC)){
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(arrayList.get(arrayList.size()-1).getStatus())-4)) / 4;
                canvas.drawLine(endX, endY, startPointxX + ((getCurrentMillis() / (3600*24f)) * CUSTOM_TABLE_WIDTH*24/28), endY, paintArray.get(getStatus(arrayList.get(arrayList.size()-1).getStatus())));
            }else if (Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_ON_YM)){
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(arrayList.get(arrayList.size()-1).getStatus())-2)) / 4;
                canvas.drawLine(endX, endY, startPointxX + ((getCurrentMillis() / (3600*24f)) * CUSTOM_TABLE_WIDTH*24/28), endY, paintArray.get(getStatus(arrayList.get(arrayList.size()-1).getStatus())));
            }else if (getStatus(arrayList.get(arrayList.size()-1).getStatus()) < 4){
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(arrayList.get(arrayList.size()-1).getStatus()))) / 4;
                canvas.drawLine(endX, endY, startPointxX + ((getCurrentMillis() / (3600*24f)) * CUSTOM_TABLE_WIDTH*24/28), endY, paintArray.get(getStatus(arrayList.get(arrayList.size()-1).getStatus())));
            }

            if (Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_OFF) || Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_OF_PC)) {
                off += (getCurrentMillis() - start);
            }
            else if (Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_SB)) {
                sb += (getCurrentMillis() - start);
            }
            else if (Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_DR)) {
                dr += (getCurrentMillis() - start);
            }
            else if (Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_ON) || Objects.equals(arrayList.get(arrayList.size() - 1).getStatus(), EnumsConstants.STATUS_ON_YM)) {
                on += (getCurrentMillis() - start);
            }
        }else {
            if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_OF_PC)){
                endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(firstStatus.getStatus())-4)) / 4;
            }else if(Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_ON_YM)){
                Log.d("Adverse",firstStatus.getStatus() + "");
                endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(firstStatus.getStatus())-2)) / 4;
            }else {
                endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * getStatus(firstStatus.getStatus())) / 4;
            }
            canvas.drawLine(startX, endY, startPointxX + CUSTOM_TABLE_WIDTH*24/28*(getCurrentMillis() / (3600*24f)), endY, paintArray.get(getStatus(firstStatus.getStatus())));

            if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_OFF) || Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_OF_PC)) {
                off = getCurrentMillis();
            }
            if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_SB)) {
                sb = getCurrentMillis();
            }
            if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_DR)) {
                dr = getCurrentMillis();
            }
            if (Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_ON) || Objects.equals(firstStatus.getStatus(), EnumsConstants.STATUS_ON_YM)) {
                on = getCurrentMillis();
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
        float CUSTOM_TABLE_HEIGHT = 0;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 6;
        }else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        }
        canvas.drawText(
                getTime(off),
                START_POINT_X + 13*CUSTOM_TABLE_WIDTH/14 + 5f,
                START_POINT_Y + 5*CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getOFFPaint(getContext())
        );
        canvas.drawText(
                getTime(sb),
                START_POINT_X + 13*CUSTOM_TABLE_WIDTH/14 + 5f,
                START_POINT_Y + 13 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getSBPaint(getContext())
        );
        canvas.drawText(
                getTime(dr),
                START_POINT_X + 13*CUSTOM_TABLE_WIDTH/14 + 5f,
                START_POINT_Y + 21 * CUSTOM_TABLE_HEIGHT / 32,
                TableConstants.getDRPaint(getContext())
        );
        canvas.drawText(
                getTime(on),
                START_POINT_X + 13*CUSTOM_TABLE_WIDTH/14 + 5f,
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

    private void drawChart(Status status,Status nextStatus,Canvas canvas,float CUSTOM_TABLE_WIDTH){

        if (getStatus(status.getStatus()) < 4){
            if (getStatus(nextStatus.getStatus()) <4){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * getStatus(status.getStatus())) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus()))) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(status.getStatus())));
                startX = endX;
            }else if (Objects.equals(nextStatus.getStatus(), EnumsConstants.STATUS_OF_PC)){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * getStatus(status.getStatus())) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus())-4)) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(status.getStatus())));
                startX = endX;
            }else if (Objects.equals(nextStatus.getStatus(), EnumsConstants.STATUS_ON_YM)){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * getStatus(status.getStatus())) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus())-2)) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(status.getStatus())));
                startX = endX;
            }
        }else if (Objects.equals(status.getStatus(), EnumsConstants.STATUS_OF_PC)){
            if (Objects.equals(nextStatus.getStatus(), EnumsConstants.STATUS_ON_YM)){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(status.getStatus())-4)) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus())-2)) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(nextStatus.getStatus())));
                startX = endX;
            }else if (getStatus(nextStatus.getStatus()) <4){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(status.getStatus())-4)) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus()))) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(status.getStatus())));
                startX = endX;
            }
        }else if (Objects.equals(status.getStatus(), EnumsConstants.STATUS_ON_YM)){
            if (Objects.equals(nextStatus.getStatus(), EnumsConstants.STATUS_OF_PC)){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(status.getStatus())-2)) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus())-4)) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(status.getStatus())));
                startX = endX;
            }  else if (getStatus(nextStatus.getStatus()) < 4){
                startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(status.getStatus())-2)) / 4;
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (getStatus(nextStatus.getStatus()))) / 4;
                endX = startPointxX + (stringToDate(nextStatus.getTime()).toLocalTime().toSecondOfDay() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                canvas.drawLine(startX, startY, endX, startY, paintArray.get(getStatus(status.getStatus())));
                canvas.drawLine(endX, startY, endX, endY, paintArray.get(getStatus(status.getStatus())));
                startX = endX;
            }
        }
    }
}
