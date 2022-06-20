package com.iosix.eldblesample.enums;

import android.annotation.SuppressLint;
import android.content.Context;
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
        paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        return paint;
    }

    public static Paint getSBPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        paint.setColor(Color.parseColor("#DD8C12"));
        paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        return paint;
    }

    public static Paint getDRPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        paint.setColor(Color.parseColor("#63A83D"));
        paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        return paint;
    }

    public static Paint getONPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(context.getResources().getDimension(R.dimen.table_stroke_with));
        paint.setColor(Color.parseColor("#851DC6"));
        paint.setTextSize(context.getResources().getDimension(R.dimen.custom_table_text_size));
        return paint;
    }
}
