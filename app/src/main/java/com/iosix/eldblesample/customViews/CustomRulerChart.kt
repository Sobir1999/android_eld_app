package com.iosix.eldblesample.customViews

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Display
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.iosix.eldblesample.R

@SuppressLint("Recycle")
class CustomRulerChart(context: Context?, attrs: AttributeSet) : View(context, attrs) {
    var table_paint = Paint()
    var table_time_paint = TextPaint()
    var table_time_paint_tex = TextPaint()
    var table_text_paint = TextPaint()
    val vm = context?.getSystemService(Context.WINDOW_SERVICE) as (WindowManager)
    var SCREEN_WIDTH : Int? = 0

    val point = Point()
    val orientation = context?.resources?.configuration?.orientation

    var CUSTOM_WIDTH = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        vm.currentWindowMetrics.bounds.width()
    } else {
        point.x * 1000
    }

//    val CUSTOM_WIDTH = 1000f

    val START_POINT_X = 100.0f
    val START_POINT_Y = 50.0f

    var CUSTOM_TABLE_WIDTH = CUSTOM_WIDTH - 2.5f * START_POINT_X

    val CUSTOM_TABLE_HEIGHT = CUSTOM_TABLE_WIDTH / 3
    val CUSTOM_TABLE_ROW_WIDTH = CUSTOM_TABLE_WIDTH / 24

    init {
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.CustomRulerChart)
//        SCREEN_WIDTH = attributes?.getInteger(R.styleable.CustomRulerChart_screenWidth, 300)
        attributes?.recycle()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

            onDrawTable(canvas)
            onDrawText(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        CUSTOM_WIDTH = w
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val timeFontMetrics = table_time_paint.fontMetrics
        val textFontMetrics = table_text_paint.fontMetrics

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



        canvas?.drawText("OFF", START_POINT_X/2 - 20.0f, START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("00:00", START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f, START_POINT_Y + CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("SB", START_POINT_X/2 - 20.0f, START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("00:00", START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f, START_POINT_Y + 3 * CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("DR", START_POINT_X/2 - 20.0f, START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("00:00", START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f, START_POINT_Y + 5 * CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("ON", START_POINT_X/2 - 20.0f, START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
        canvas?.drawText("00:00", START_POINT_X + CUSTOM_TABLE_WIDTH + 10.0f, START_POINT_Y + 7 * CUSTOM_TABLE_HEIGHT / 8, table_text_paint)
    }

    private fun onDrawTableHorizantal(canvas: Canvas?){}
}
