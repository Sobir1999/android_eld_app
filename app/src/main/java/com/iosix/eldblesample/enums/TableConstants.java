package com.iosix.eldblesample.enums;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompatApi26Impl;

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
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTypeface(ResourcesCompat.getFont(context,R.font.montserrat));
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
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.parseColor("#DD8C12"));
        paint.setTypeface(ResourcesCompat.getFont(context,R.font.montserrat));
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
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTypeface(ResourcesCompat.getFont(context,R.font.montserrat));
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
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTypeface(ResourcesCompat.getFont(context,R.font.montserrat));
        paint.setColor(Color.parseColor("#851DC6"));
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size_land));
        }else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        }
        return paint;
    }

    public static Paint getOFFPCPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(MaterialColors.getColor(context,R.attr.customPaint,Color.BLACK));
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{15,5},0));
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }

    public static Paint getONYMPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#851DC6"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{15,5},0));
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }

    public static Paint getLoginPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#1B5E20"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }

    public static Paint getLogoutPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#C2185B"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }

    public static Paint getCertifiedPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#00C853"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }

    public static Paint getPowerUpPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#33691E"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }

    public static Paint getPowerDownPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#D32F2F"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        return paint;
    }
}
