package com.iosix.eldblesample.enums;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

public class TableConstants {
    public static float START_POINT_X = 25.0f;
    public static float START_POINT_Y = 50.0f;
    private static Paint paint;
    public static float CUSTOM_WIDTH;
    

    public static Paint getOFFPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setColor(Color.parseColor("#8C8C8C"));
        paint.setStrokeWidth(CUSTOM_WIDTH*1.5f);
        paint.setTextSize(CUSTOM_WIDTH*10f);
        return paint;
    }

    public static Paint getSBPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(CUSTOM_WIDTH*1.5f);
        paint.setColor(Color.parseColor("#DD8C12"));
        paint.setTextSize(CUSTOM_WIDTH*10f);
        return paint;
    }

    public static Paint getDRPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(CUSTOM_WIDTH*1.5f);
        paint.setColor(Color.parseColor("#63A83D"));
        paint.setTextSize(CUSTOM_WIDTH*10f);
        return paint;
    }

    public static Paint getONPaint(Context context) {
        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().scaledDensity;
        paint = new Paint();
        paint.setStrokeWidth(CUSTOM_WIDTH*1.5f);
        paint.setColor(Color.parseColor("#851DC6"));
        paint.setTextSize(CUSTOM_WIDTH*10f);
        return paint;
    }
}
