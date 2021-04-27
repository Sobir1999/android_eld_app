package com.iosix.eldblesample.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.text.TextPaint
import android.text.format.Time
import android.util.AttributeSet
import android.view.View
import com.iosix.eldblesample.R
import java.util.*

@SuppressLint("Recycle")
class CustomRulerChart(context: Context?, attrs: AttributeSet) : View(context, attrs), Runnable {

    private val DELAY_TIME_MILLIS = 1000L
    private var updateView = false
    private val count: Long? = null
    private val endX = 0f
    private val endY = 0f

    var canvas: Canvas? = null

    var table_paint = Paint()
    var table_time_paint = Paint()
    var table_time_paint_tex = Paint()
    var table_text_paint = Paint()

    val orientation = context?.resources?.configuration?.orientation

    var CUSTOM_WIDTH = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        context?.getResources()?.getDisplayMetrics()?.heightPixels
    } else {
        context?.getResources()?.getDisplayMetrics()?.widthPixels
    }

    val START_POINT_X = 100.0f
    val START_POINT_Y = 50.0f

    var CUSTOM_TABLE_WIDTH = CUSTOM_WIDTH!! - 2.5f * START_POINT_X

    val CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 3
    val CUSTOM_TABLE_ROW_WIDTH = CUSTOM_TABLE_WIDTH / 24

    init {
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.CustomRulerChart)
        attributes?.recycle()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        onDrawTable(canvas)
        onDrawText(canvas)

        drawLineProgress(canvas, 0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        CUSTOM_WIDTH = w

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredWidth = Math.round(START_POINT_X + START_POINT_X + CUSTOM_TABLE_WIDTH + paddingRight + paddingLeft)
        val desiredHeight = Math.round(START_POINT_Y + CUSTOM_TABLE_HEIGHT + paddingTop + paddingBottom)

        val mesaureWidth = reconcileSize(desiredWidth, widthMeasureSpec)
        val measureHeight = reconcileSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(mesaureWidth, measureHeight)
    }

    private fun reconcileSize(contentSize: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> if (contentSize < specSize) {
                contentSize
            } else {
                specSize
            }
            MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    private fun onDrawTable(canvas: Canvas?) {
        table_paint.color = Color.BLACK
        table_paint.style = Paint.Style.STROKE
        table_paint.strokeWidth = 1.0f

        canvas?.drawRect(
            START_POINT_X,
            START_POINT_Y,
            START_POINT_X + CUSTOM_TABLE_WIDTH,
            START_POINT_Y + CUSTOM_TABLE_HEIGHT,
            table_paint
        )

        for (i in 1..23) {
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i,
                START_POINT_Y,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                table_paint
            )
        }

        for (i in 1..3) {
            canvas?.drawLine(
                START_POINT_X,
                START_POINT_Y + i * CUSTOM_TABLE_HEIGHT / 4,
                START_POINT_X + CUSTOM_TABLE_WIDTH,
                START_POINT_Y + i * CUSTOM_TABLE_HEIGHT / 4,
                table_paint
            )
        }

        for (i in 0..23) {
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 16,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 16,
                table_paint
            )

            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4 + CUSTOM_TABLE_HEIGHT / 16,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4 + CUSTOM_TABLE_HEIGHT / 8,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT / 4 + CUSTOM_TABLE_HEIGHT / 16,
                table_paint
            )

            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4 - CUSTOM_TABLE_HEIGHT / 16,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4 - CUSTOM_TABLE_HEIGHT / 8,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4 - CUSTOM_TABLE_HEIGHT / 16,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 4,
                table_paint
            )

            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT - CUSTOM_TABLE_HEIGHT / 16,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT - CUSTOM_TABLE_HEIGHT / 8,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + CUSTOM_TABLE_ROW_WIDTH / 2,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                table_paint
            )
            canvas?.drawLine(
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT - CUSTOM_TABLE_HEIGHT / 16,
                START_POINT_X + CUSTOM_TABLE_ROW_WIDTH * i + 3 * CUSTOM_TABLE_ROW_WIDTH / 4,
                START_POINT_Y + CUSTOM_TABLE_HEIGHT,
                table_paint
            )
        }
    }

    private fun onDrawText(canvas: Canvas?) {
        table_time_paint.color = Color.BLACK
        val time_size = 16f
        table_time_paint.textSize = time_size

        table_time_paint_tex.color = Color.BLACK
        table_time_paint_tex.textSize = 1.5f * time_size

        table_text_paint.color = Color.BLACK
        table_text_paint.textSize = 26f
        table_text_paint.strokeWidth = 3f

        for (i in 0..24) {
            if (i == 0 || i == 24) {
                canvas?.drawText(
                    "M",
                    START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                    START_POINT_Y - 5f,
                    table_time_paint_tex
                )
            } else if (i == 12) {
                canvas?.drawText(
                    "N",
                    START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                    START_POINT_Y - 5f,
                    table_time_paint_tex
                )
            } else {
                canvas?.drawText(
                    "${i % 12}",
                    START_POINT_X - time_size / 2 + CUSTOM_TABLE_ROW_WIDTH * i,
                    START_POINT_Y - 5f,
                    table_time_paint
                )
            }
        }



        canvas?.drawText(
            "OFF",
            START_POINT_X / 2 - 20.0f,
            START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "00:00",
            START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
            START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "SB",
            START_POINT_X / 2 - 20.0f,
            START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "00:00",
            START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
            START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "DR",
            START_POINT_X / 2 - 20.0f,
            START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "00:00",
            START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
            START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "ON",
            START_POINT_X / 2 - 20.0f,
            START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
        canvas?.drawText(
            "00:00",
            START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f,
            START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8,
            table_text_paint
        )
    }

    private fun drawLineProgress(canvas: Canvas?, row: Int) {
        val time = Time()
        time.setToNow()

        val i: Long = (time.hour * 3600 + time.minute * 60 + time.second).toLong()

        canvas?.drawLine(
            START_POINT_X,
            START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
            START_POINT_X + i*8/CUSTOM_TABLE_WIDTH,
            START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8,
            table_paint
        );

        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateView = true
    }

    override fun onDetachedFromWindow() {
        updateView = false
        super.onDetachedFromWindow()
    }

    override fun run() {
        drawLineProgress(canvas, 0)
    }
}
