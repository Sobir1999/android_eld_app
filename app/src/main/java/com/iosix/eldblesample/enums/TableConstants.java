package com.iosix.eldblesample.enums;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.material.color.MaterialColors;
import com.iosix.eldblesample.R;

public class TableConstants {
    public static float START_POINT_X = 25.0f;
    public static float START_POINT_Y = 50.0f;
    private static Paint paint;
    public static float CUSTOM_WIDTH;


    public static Paint getOFFPaint(Context context) {

        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(MaterialColors.getColor(context,R.attr.customPaint,Color.BLACK));
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size_land));
        }else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        }
        return paint;
    }

    public static Paint getSBPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        paint.setColor(Color.parseColor("#DD8C12"));
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size_land));
        }else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        }
        return paint;
    }

    public static Paint getDRPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        paint.setColor(Color.parseColor("#63A83D"));
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size_land));
        }else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        }
        return paint;
    }

    public static Paint getONPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        paint.setColor(Color.parseColor("#851DC6"));
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size_land));
        }else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        }
        return paint;
    }
}
