package com.test.novel.view.customView


import android.content.Context
import android.graphics.Canvas
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NovelTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr)   {

    private val textPaint = TextPaint()

    init {
        textPaint.textSize = textSize
        textPaint.color = currentTextColor
        textPaint.typeface = typeface

    }

    override fun onDraw(canvas: Canvas) {

    }
}
