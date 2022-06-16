package com.iosix.eldblesample.customViews;

import static com.iosix.eldblesample.MyApplication.userData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.TableConstants;

public abstract class   CustomRulerChart extends View {
    private final Paint table_paint = new Paint();
    private final Paint table_time_paint = new Paint();
    private final Paint table_time_paint_tex = new Paint();
    private final Paint table_text_paint = new Paint();

    private int CUSTOM_WIDTH;

    private final float START_POINT_X = TableConstants.START_POINT_X;
    private final float START_POINT_Y = TableConstants.START_POINT_Y;

    private float CUSTOM_TABLE_WIDTH;

    private float CUSTOM_TABLE_HEIGHT;
    private float CUSTOM_TABLE_ROW_WIDTH;
    private float CUSTOM_TABLE_STROKE_WIDTH;

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

        CUSTOM_WIDTH = context.getResources().getDisplayMetrics().widthPixels;

        CUSTOM_TABLE_WIDTH = CUSTOM_WIDTH - 2f * START_POINT_X - 100f*context.getResources().getDisplayMetrics().density;

        CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 8;
        CUSTOM_TABLE_ROW_WIDTH = CUSTOM_TABLE_WIDTH / 26;

        CUSTOM_TABLE_STROKE_WIDTH = context.getResources().getDisplayMetrics().density * 1.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawTable(canvas);
        onDrawText(canvas);

        drawLineProgress(canvas, CUSTOM_TABLE_WIDTH);
        drawTextTime(canvas, CUSTOM_TABLE_WIDTH);
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

        int desiredWidth = Math.round(START_POINT_X + CUSTOM_TABLE_WIDTH + getPaddingRight() + getPaddingLeft());
        int desiredHeight = Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + getPaddingTop() + getPaddingBottom());

        int measureWith = reconcileSize(desiredWidth, widthMeasureSpec);
        int measureHeight = reconcileSize(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(measureWith, measureHeight);
    }

    @SuppressLint("SwitchIntDef")
    private int reconcileSize(int contentSize, int measureSpace) {
        int mode = MeasureSpec.getMode(measureSpace);
        int specSize = MeasureSpec.getSize(measureSpace);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                return Math.min(contentSize, specSize);
            default:
                return contentSize;
        }
    }

    private void onDrawTable(Canvas canvas) {
        if (!userData.getMode()){
            table_paint.setColor(getResources().getColor(R.color.customTableColor));
        }else {
            table_paint.setColor(Color.WHITE);
        }
        table_paint.setStyle(Paint.Style.STROKE);
        table_paint.setStrokeWidth(CUSTOM_TABLE_STROKE_WIDTH);

        canvas.drawRect(
                START_POINT_X,
                START_POINT_Y,
                START_POINT_X + CUSTOM_TABLE_WIDTH,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                table_paint
        );

        for (int i = 1; i <= 26; i++) {
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

        for (int i = 1; i <= 24; i++) {
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

        float time_size = 20f;
        table_time_paint.setTextSize(time_size);

        table_time_paint.setColor(getResources().getColor(R.color.colorPrimary));
        table_time_paint_tex.setColor(getResources().getColor(R.color.colorPrimary));
        table_time_paint.setTypeface(Typeface.DEFAULT_BOLD);
        table_time_paint_tex.setTypeface(Typeface.DEFAULT_BOLD);

        table_time_paint_tex.setTextSize(time_size);

        table_text_paint.setColor(Color.BLACK);
        table_text_paint.setTextSize(26f);
        table_text_paint.setStrokeWidth(3f);

        for (int i = 0; i <= 26; i++) {
            if (i == 1 || i == 25) {
                canvas.drawText(
                        "M",
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 10f,
                        table_time_paint_tex
                );
            } else if (i == 13) {
                canvas.drawText(
                        "N",
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 10f,
                        table_time_paint_tex
                );
            }else if (i == 0){
                canvas.drawText(
                        "OFF",
                        START_POINT_X +CUSTOM_TABLE_ROW_WIDTH/4,
                        START_POINT_Y + 5*CUSTOM_TABLE_HEIGHT / 32,
                        TableConstants.getOFFPaint(getContext())
                );

                canvas.drawText(
                        "SB",
                        START_POINT_X +CUSTOM_TABLE_ROW_WIDTH/4,
                        START_POINT_Y + 13 * CUSTOM_TABLE_HEIGHT / 32,
                        TableConstants.getSBPaint(getContext())
                );

                canvas.drawText(
                        "DR",
                        START_POINT_X +CUSTOM_TABLE_ROW_WIDTH/4,
                        START_POINT_Y + 21 * CUSTOM_TABLE_HEIGHT / 32,
                        TableConstants.getDRPaint(getContext())
                );

                canvas.drawText(
                        "ON",
                        START_POINT_X +CUSTOM_TABLE_ROW_WIDTH/4,
                        START_POINT_Y + 29 * CUSTOM_TABLE_HEIGHT / 32,
                        TableConstants.getONPaint(getContext())
                );
            }
            else if (i<13){
                canvas.drawText(
                        "" + ((i % 13)-1),
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 10f,
                        table_time_paint
                );
            }else {
                canvas.drawText(
                        "" + i % 13,
                        START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                        START_POINT_Y - 10f,
                        table_time_paint
                );
            }
        }

    }

    public abstract void drawLineProgress(Canvas canvas, float CUSTOM_TABLE_WIDTH);

    public abstract void drawTextTime(Canvas canvas, float CUSTOM_TABLE_WIDTH);
}
