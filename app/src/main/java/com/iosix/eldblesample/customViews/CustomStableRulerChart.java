package com.iosix.eldblesample.customViews;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.TableConstants;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;

import java.util.ArrayList;

public class CustomStableRulerChart extends CustomRulerChart {
    private float START_POINT_X = TableConstants.START_POINT_X;
    private float START_POINT_Y = TableConstants.START_POINT_Y;
    private int last_status;
    private int off = 0, sb = 0, dr = 0, on = 0;

    private ArrayList<LogEntity> arrayList = new ArrayList<>();
    private ArrayList<LogEntity> arrayList1 = new ArrayList<>();
    private DriverSharedPrefs driverSharedPrefs;

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
        driverSharedPrefs = DriverSharedPrefs.getInstance(context);
    }

    public void setArrayList(ArrayList<LogEntity> arrayList,ArrayList<LogEntity> arrayList1) {
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;
    }

    @Override
    public void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH) {
        float CUSTOM_TABLE_HEIGHT = 0;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 6;
        }else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        }
        float startX = START_POINT_X+CUSTOM_TABLE_WIDTH / 14;
        float startPointX = START_POINT_X+CUSTOM_TABLE_WIDTH / 14;
        float startY = 0;
        float endX = 0;
        float endY = 0;
        int off = 0, sb = 0, dr = 0, on = 0;

        int start = 0;

        for (int i = 0; i < arrayList.size() - 1; i++) {

            if (arrayList.get(i).getTo_status() == EnumsConstants.STATUS_OF_PC){
                if (arrayList.get(i+1).getTo_status() == EnumsConstants.STATUS_ON_YM){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i).getTo_status()-4)) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status()-2)) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }else if (arrayList.get(i+1).getTo_status() <4){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i).getTo_status()-4)) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status())) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }
            }else if (arrayList.get(i).getTo_status() == EnumsConstants.STATUS_ON_YM){
                if (arrayList.get(i+1).getTo_status() == EnumsConstants.STATUS_OF_PC){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i).getTo_status()-2)) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status()-4)) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }  else if (arrayList.get(i+1).getTo_status() < 4){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i).getTo_status()-2)) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status())) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }
            }else if (arrayList.get(i).getTo_status() < 4){
                if (arrayList.get(i+1).getTo_status() == EnumsConstants.STATUS_OF_PC){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status()-4)) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }else if (arrayList.get(i+1).getTo_status() == EnumsConstants.STATUS_ON_YM){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status()-2)) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }else if (arrayList.get(i+1).getTo_status() < 4){
                    startY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * arrayList.get(i).getTo_status()) / 4;
                    endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(i+1).getTo_status())) / 4;
                    endX = startPointX + (arrayList.get(i+1).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28);
                    canvas.drawLine(startX, startY, endX, startY, paintArray.get(arrayList.get(i).getTo_status()));
                    canvas.drawLine(endX, startY, endX, endY, paintArray.get(arrayList.get(i).getTo_status()));
                    startX = endX;
                }
            }


            if (arrayList.get(i).getTo_status() == EnumsConstants.STATUS_OFF || arrayList.get(i).getTo_status() == EnumsConstants.STATUS_OF_PC) {
                off += (arrayList.get(i+1).getSeconds() - start);
            }
            else if (arrayList.get(i).getTo_status() == EnumsConstants.STATUS_SB) {
                sb += (arrayList.get(i+1).getSeconds() - start);
            }
            else if (arrayList.get(i).getTo_status() == EnumsConstants.STATUS_DR) {
                dr += (arrayList.get(i+1).getSeconds() - start);
            }
            else if (arrayList.get(i).getTo_status() == EnumsConstants.STATUS_ON || arrayList.get(i).getTo_status() == EnumsConstants.STATUS_ON_YM) {
                on += (arrayList.get(i+1).getSeconds() - start);
            }
            start = arrayList.get(i+1).getSeconds();
        }

        for (int i = 0; i < arrayList1.size(); i++) {
            if (arrayList1.get(i).getTo_status() == EnumsConstants.LOGIN){
                canvas.drawLine(startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(arrayList1.get(i).getTo_status()));
                Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_login_circle_svgrepo_com,null);
                icon.setBounds(Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
                icon.setTint(Color.parseColor("#1B5E20"));
                icon.draw(canvas);
            }else if (arrayList1.get(i).getTo_status() == EnumsConstants.LOGOUT){
                canvas.drawLine(startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(arrayList1.get(i).getTo_status()));
                Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_circle_logout_svgrepo_com,null);
                icon.setBounds(Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 15f),Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 20f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
                icon.setTint(Color.parseColor("#C2185B"));
                icon.draw(canvas);
            }else if (arrayList1.get(i).getTo_status() == EnumsConstants.CERTIFIED){
                canvas.drawLine(startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(arrayList1.get(i).getTo_status()));
                Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_check_circle_8,null);
                icon.setBounds(Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
                icon.setTint(Color.parseColor("#00C853"));
                icon.draw(canvas);
            }else if (arrayList1.get(i).getTo_status() == EnumsConstants.POWER_UP){
                canvas.drawLine(startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y,startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(arrayList1.get(i).getTo_status()));
                Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_power_sign_in_a_circle_svgrepo_com,null);
                icon.setBounds(Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
                icon.setTint(Color.parseColor("#33691E"));
                icon.draw(canvas);
            }else if (arrayList1.get(i).getTo_status() == EnumsConstants.POWER_DOWN){
                canvas.drawLine(startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/26),START_POINT_Y,startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28),START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f,paintArray.get(arrayList1.get(i).getTo_status()));
                Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_power_sign_in_a_circle_svgrepo_com,null);
                icon.setBounds(Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))- 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 20f),Math.round((startPointX + (arrayList1.get(i).getSeconds() / (3600*24f))*(CUSTOM_TABLE_WIDTH*24/28))+ 15f),Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + 50f));
                icon.setTint(Color.parseColor("#D32F2F"));
                icon.draw(canvas);
            }
        }


        if (!arrayList.isEmpty()) {
            if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_OF_PC){
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(arrayList.size()-1).getTo_status()-4)) / 4;
                canvas.drawLine(startX, endY, startPointX + ((getCurrentSeconds() / (3600*24f)) * CUSTOM_TABLE_WIDTH*24/28), endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));
            }else if (arrayList.get(arrayList.size()-1).getTo_status() == EnumsConstants.STATUS_ON_YM){
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(arrayList.size()-1).getTo_status()-2)) / 4;
                canvas.drawLine(startX, endY, startPointX + ((getCurrentSeconds() / (3600*24f)) * CUSTOM_TABLE_WIDTH*24/28), endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));
            }else if (arrayList.get(arrayList.size()-1).getTo_status() < 4){
                endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * (arrayList.get(arrayList.size()-1).getTo_status())) / 4;
                canvas.drawLine(startX, endY, startPointX + CUSTOM_TABLE_WIDTH*24/28, endY, paintArray.get(arrayList.get(arrayList.size() - 1).getTo_status()));
            }

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
            driverSharedPrefs.saveWorkingHours(arrayList.get(0).getDriverId() + " " + arrayList.get(0).getTime(),dr + on);

        } else {
            endY = TableConstants.START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8 + (CUSTOM_TABLE_HEIGHT * EnumsConstants.STATUS_OFF) / 4;
            canvas.drawLine(startX, endY, startPointX + CUSTOM_TABLE_WIDTH*24/28, endY, paintArray.get(EnumsConstants.STATUS_OFF));

                off = 86400;
        }

        this.off = off;
        this.sb = sb;
        this.dr = dr;
        this.on = on;
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
        String s = "";
        int hour = sum / 3600;
        int min = (sum % 3600) / 60;
        s = String.format("%02d:%02d", hour, min);
        return s;
    }
}
