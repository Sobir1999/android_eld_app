package com.iosix.eldblesample.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.iosix.eldblesample.R

@SuppressLint("CustomViewStyleable", "ResourceAsColor")
class StatusCustomButtonView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
    var color: Int = R.styleable.CustomView_color

    init {
        inflate(context, R.layout.custom_button_view_item, this)

        val imageView: ImageView = findViewById(R.id.image)
        val status: TextView = findViewById(R.id.status)
        val card: CardView = findViewById(R.id.card)
        val title: TextView = findViewById(R.id.title)


        imageView.setImageDrawable(attributes.getDrawable(R.styleable.CustomView_image))
        status.text = attributes.getString(R.styleable.CustomView_status)
        card.setCardBackgroundColor(attributes.getColor(color, R.color.colorBackgroundMainView))
        title.text = attributes.getString(R.styleable.CustomView_title)
        attributes.recycle()
    }

    fun setColorCustom(color: Int) {
        this.color = color
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        this.color = color
    }
}