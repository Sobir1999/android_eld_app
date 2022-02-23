package com.iosix.eldblesample.enums;

import android.graphics.Color;
import android.graphics.Paint;

public class TableConstants {
    public static float START_POINT_X = 12.0f;
    public static float START_POINT_Y = 50.0f;
    private static Paint paint;
    
    private static float textWidth = 3.5f;
    private static float textSize = 23f;

    public static Paint getOFFPaint() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#8C8C8C"));
        paint.setStrokeWidth(textWidth);
        paint.setTextSize(textSize);
        return paint;
    }

    public static Paint getSBPaint() {
        paint = new Paint();
        paint.setStrokeWidth(textWidth);
        paint.setColor(Color.parseColor("#DD8C12"));
        paint.setTextSize(textSize);
        return paint;
    }

    public static Paint getDRPaint() {
        paint = new Paint();
        paint.setStrokeWidth(textWidth);
        paint.setColor(Color.parseColor("#63A83D"));
        paint.setTextSize(textSize);
        return paint;
    }

    public static Paint getONPaint() {
        paint = new Paint();
        paint.setStrokeWidth(textWidth);
        paint.setColor(Color.parseColor("#851DC6"));
        paint.setTextSize(textSize);
        return paint;
    }
}
