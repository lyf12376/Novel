package com.test.novel.view.customView


import android.content.Context
import android.graphics.Canvas
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NovelTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr)   {

    private val textPaint = TextPaint()
    private val lineText:MutableList<String> = mutableListOf()
    private val lineEndIndex:MutableList<Int> = mutableListOf()
    private var lineSpacing = 0f
    private val symbols:List<Char> = listOf('。','？','！','，','、','；','：','’','”','）','》')

    init {
        lineSpacing = lineSpacingExtra
        textPaint.textSize = textSize
        textPaint.color = currentTextColor
        textPaint.typeface = typeface
        textPaint.letterSpacing = letterSpacing
    }


    override fun onDraw(canvas: Canvas) {
        val x = paddingLeft.toFloat()
        var lineY = paddingTop - textPaint.fontMetrics.ascent
        val text = text.toString()
        val maxWidth = width - paddingRight - paddingLeft
        val textSizePx = textPaint.textSize
        val letterSpacePx = textSizePx * textPaint.letterSpacing
        var lineStart = 0
        val fontMetrics = textPaint.fontMetrics
        val lineHeight = fontMetrics.descent - fontMetrics.ascent + lineSpacing

        while (lineStart < text.length) {
            textPaint.letterSpacing = letterSpacing
            val count = textPaint.breakText(
                text, lineStart, text.length, true,
                maxWidth.toFloat(), null
            )
            val endIndex = (lineStart + count).coerceAtMost(text.length)

            if (lineStart + count == text.length) {
                val thisLine = text.substring(lineStart, endIndex)
                lineEndIndex.add(endIndex)
                lineText.add(thisLine)
                canvas.drawText(thisLine, x, lineY, textPaint)
                break
            } else if (text.substring(lineStart, endIndex).contains("\n")) {
                val lineEnd = lineStart + text.substring(lineStart, endIndex).indexOf("\n") + 1
                val thisLine = text.substring(lineStart, lineEnd)
                lineEndIndex.add(lineEnd)
                lineText.add(thisLine)
                canvas.drawText(thisLine, x, lineY, textPaint)
                lineStart = lineEnd
                lineY += lineHeight
            } else if (lineStart + count < text.length && symbols.contains(text[lineStart + count])) {
                val lineEnd = (lineStart + count + 1).coerceAtMost(text.length)
                val thisLine = text.substring(lineStart, lineEnd)
                lineEndIndex.add(lineEnd)
                lineText.add(thisLine)
                val textWidth = textPaint.measureText(thisLine)
                val space = if (thisLine.length > 1) (maxWidth - textWidth) / (thisLine.length - 1) else 0f
                textPaint.letterSpacing += space / textSizePx
                canvas.drawText(thisLine, x, lineY, textPaint)
                lineStart = lineEnd
                lineY += lineHeight
            } else {
                val lineEnd = Math.min(lineStart + count, text.length)
                val thisLine = text.substring(lineStart, lineEnd)
                lineEndIndex.add(lineEnd)
                lineText.add(thisLine)
                val textWidth = textPaint.measureText(thisLine)
                val space = if (thisLine.length > 1) (maxWidth - textWidth) / (thisLine.length - 1) else 0f
                textPaint.letterSpacing += space / textSizePx
                canvas.drawText(thisLine, x, lineY, textPaint)
                lineStart = lineEnd
                lineY += lineHeight
            }
        }
    }


    fun getLineCountCus(): Int {
        return lineText.size
    }

    fun getLineText(line: Int): String {
        return lineText[line]
    }

    fun getLineEnd(line: Int): Int {
        return lineEndIndex[line]
    }

}
