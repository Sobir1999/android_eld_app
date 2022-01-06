package com.iosix.eldblesample.enums;

import android.graphics.Color;
import android.graphics.Paint;

public class TableConstants {
    public static float START_POINT_X = 80.0f;
    public static float START_POINT_Y = 50.0f;
    private static Paint paint;
    private static float textWidth = 3f;
    private static float textSize = 26f;

    public static Paint getOFFPaint() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#6E6565"));
        paint.setStrokeWidth(textWidth);
        paint.setTextSize(textSize);
        return paint;
    }

    public static Paint getSBPaint() {
        paint = new Paint();
        paint.setStrokeWidth(textWidth);
        paint.setColor(Color.parseColor("#DAC50C"));
        paint.setTextSize(textSize);
        return paint;
    }

    public static Paint getDRPaint() {
        paint = new Paint();
        paint.setStrokeWidth(textWidth);
        paint.setColor(Color.parseColor("#1DDA82"));
        paint.setTextSize(textSize);
        return paint;
    }

    public static Paint getONPaint() {
        paint = new Paint();
        paint.setStrokeWidth(textWidth);
        paint.setColor(Color.parseColor("#1A8DE8"));
        paint.setTextSize(textSize);
        return paint;
    }
}
