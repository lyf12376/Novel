package com.test.novel.view.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.TextView


class NovelTextView(context: Context, attrs: AttributeSet? = null):androidx.appcompat.widget.AppCompatTextView(context,attrs) {
    var novelLineHeight = 0f
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 2f
    }

    private val paint1 = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLUE
        strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
            val layout = layout
            val lineCount = layout.lineCount
            for (i in 0 until lineCount) {
                val top = layout.getLineTop(i).toFloat() + paddingTop
                val bottom = layout.getLineBottom(i).toFloat() + paddingTop
                println(top)
                println(bottom)
                novelLineHeight = bottom - top
//                println(top-bottom)
                // 绘制每行文字的上边界
                canvas.drawLine(0f, top, width.toFloat(), top, paint)
                // 绘制每行文字的下边界
                canvas.drawLine(0f, bottom, width.toFloat(), bottom, paint1)
            }
        }
    }


}