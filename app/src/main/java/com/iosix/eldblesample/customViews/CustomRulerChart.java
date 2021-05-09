package com.iosix.eldblesample.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.iosix.eldblesample.models.ExampleModel;
//import com.iosix.eldblesample.models.ExampleTimeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CustomRulerChart extends View {
    private Paint table_paint = new Paint();
    private Paint table_time_paint = new Paint();
    private Paint table_time_paint_tex = new Paint();
    private Paint table_text_paint = new Paint();

    private int CUSTOM_WIDTH;

    private float START_POINT_X = 100.0f;
    private float START_POINT_Y = 50.0f;

    private float CUSTOM_TABLE_WIDTH;

    private float CUSTOM_TABLE_HEIGHT;
    private float CUSTOM_TABLE_ROW_WIDTH;

    private Handler handler = new Handler();
    private int currentDate;
    private Canvas canvas;

//    private ArrayList<ExampleTimeModel> arrayList = new ArrayList<ExampleTimeModel>(){{
//        add(new ExampleTimeModel(0, 4343));
//        add(new ExampleTimeModel(2, 5000));
//        add(new ExampleTimeModel(1, 10000));
//        add(new ExampleTimeModel(3, 30000));
//        add(new ExampleTimeModel(0, 45000));
//        add(new ExampleTimeModel(3, 65000));
//        add(new ExampleTimeModel(1, 86399));
//    }};

    public CustomRulerChart(Context context) {
        super(context);
        init(context);
    }

    public CustomRulerChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRulerChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        canvas = new Canvas();

        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().widthPixels;

        CUSTOM_TABLE_WIDTH = CUSTOM_WIDTH - 2.5f * START_POINT_X;

        CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 3;
        CUSTOM_TABLE_ROW_WIDTH = CUSTOM_TABLE_WIDTH / 24;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        onDrawTable(canvas);
        onDrawText(canvas);
//        drawLineProgress(canvas, 0);
//        drawLine(canvas, arrayList);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        CUSTOM_WIDTH = w;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = Math.round(START_POINT_X + START_POINT_X + CUSTOM_TABLE_WIDTH + getPaddingRight() + getPaddingLeft());
        int desiredHeight = Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + getPaddingTop() + getPaddingBottom());

        int measureWith = reconcileSize(desiredWidth, widthMeasureSpec);
        int measureHeight = reconcileSize(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(measureWith, measureHeight);
    }

    private int reconcileSize(int contentSize, int measureSpace) {
        int mode = MeasureSpec.getMode(measureSpace);
        int specSize = MeasureSpec.getSize(measureSpace);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                if (contentSize < specSize) {
                    return contentSize;
                } else {
                    return specSize;
                }
            default:
                return contentSize;
        }
    }

    private void onDrawTable(Canvas canvas) {
        table_paint.setColor(Color.BLACK);
        table_paint.setStyle(Paint.Style.STROKE);
        table_paint.setStrokeWidth(1f);

        canvas.drawRect(
                START_POINT_X,
                START_POINT_Y,
                START_POINT_X + CUSTOM_TABLE_WIDTH,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                table_paint
        );

        for (int i = 1; i <= 23; i++) {
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i,
                    START_POINT_Y,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                    table_paint
            );
        }

        for (int i = 1; i <= 3; i++) {
            canvas.drawLine(
                    START_POINT_X,
                    START_POINT_Y + i * CUSTOM_TABLE_HEIGHT / 4,
                    START_POINT_X + CUSTOM_TABLE_WIDTH,
                    START_POINT_Y + i * CUSTOM_TABLE_HEIGHT / 4,
                    table_paint
            );
        }

        for (int i = 0; i <= 23; i++) {
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 16,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 16,
                    table_paint
            );

            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4 + CUSTOM_TABLE_HEIGHT / 16,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4 + CUSTOM_TABLE_HEIGHT / 8,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4 + CUSTOM_TABLE_HEIGHT / 16,
                    table_paint
            );

            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4 - CUSTOM_TABLE_HEIGHT / 16,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4 - CUSTOM_TABLE_HEIGHT / 8,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4 - CUSTOM_TABLE_HEIGHT / 16,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4,
                    table_paint
            );

            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT - CUSTOM_TABLE_HEIGHT / 16,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT - CUSTOM_TABLE_HEIGHT / 8,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                    table_paint
            );
            canvas.drawLine(
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT - CUSTOM_TABLE_HEIGHT / 16,
                    START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                    START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                    table_paint
            );
        }
    }

    private void onDrawText(Canvas canvas) {
        table_time_paint.setColor(Color.BLACK);
        float time_size = 16f;
        table_time_paint.setTextSize(time_size);

        table_time_paint_tex.setColor(Color.BLACK);
        table_time_paint_tex.setTextSize(1.5f * time_size);

        table_text_paint.setColor(Color.BLACK);
        table_text_paint.setTextSize(26f);
        table_text_paint.setStrokeWidth(3f);

        for (int i = 0; i <= 24; i++) {
            if (i == 0 || i == 24) {
                canvas.drawText(
                        "M",
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 5f,
                        table_time_paint_tex
                );
            } else if (i == 12) {
                canvas.drawText(
                        "N",
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 5f,
                        table_time_paint_tex
                );
            } else {
                canvas.drawText(
                        "" + i % 12,
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 5f,
                        table_time_paint
                );
            }
        }

        canvas.drawText(
                "OFF",
                START_POINT_X / 2 - 20.0f,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "00:00",
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "SB",
                START_POINT_X / 2 - 20.0f,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "00:00",
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "DR",
                START_POINT_X / 2 - 20.0f,
                START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "00:00",
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "ON",
                START_POINT_X / 2 - 20.0f,
                START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
        canvas.drawText(
                "00:00",
                START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
                START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8,
                table_text_paint
        );
    }

    public void drawLineProgress(int row) {
        super.draw(canvas);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int hour = Calendar.getInstance().getTime().getHours();
                int minut = Calendar.getInstance().getTime().getMinutes();
                int second = Calendar.getInstance().getTime().getSeconds();
                currentDate = hour*3600 + minut*60 + second;
            }
        }, 5000);

        canvas.drawLine(
                START_POINT_X,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                START_POINT_X + (currentDate*8) / CUSTOM_TABLE_WIDTH,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                table_paint
        );

        invalidate();
    }

//    private void drawLine(Canvas canvas, ArrayList<ExampleTimeModel> arrayList) {
//        table_paint.setColor(Color.RED);
//        table_paint.setStrokeWidth(2);
//        float startX = START_POINT_X;
//        float endX = 0;
//        float endY = 0;
//
//        for (int i=0; i<arrayList.size(); i++) {
//            endX = START_POINT_X + (arrayList.get(i).getTime()*8)/CUSTOM_TABLE_WIDTH;
//            endY = START_POINT_Y + CUSTOM_TABLE_HEIGHT/8 + (CUSTOM_TABLE_HEIGHT*arrayList.get(i).getStatus())/4;
//            canvas.drawLine(startX, endY, endX, endY, table_paint);
//            if (i > 0) {
//                float verX = START_POINT_X + (arrayList.get(i-1).getTime()*8)/CUSTOM_TABLE_WIDTH;
//                float verStartY = START_POINT_Y + CUSTOM_TABLE_HEIGHT/8 + (CUSTOM_TABLE_HEIGHT*arrayList.get(i-1).getStatus())/4;
//                float verEndY = START_POINT_Y + CUSTOM_TABLE_HEIGHT/8 + (CUSTOM_TABLE_HEIGHT*arrayList.get(i).getStatus())/4;
//                canvas.drawLine(verX, verStartY, verX, verEndY, table_paint);
//            }
//            startX = endX;
//        }
//
//    }
}
