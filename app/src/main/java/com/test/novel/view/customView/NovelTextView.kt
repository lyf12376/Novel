package com.test.novel.view.customView


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.test.novel.R

class NovelTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint()
    private val lineText: MutableList<String> = mutableListOf()
    private val lineEndIndex: MutableList<Int> = mutableListOf()
    private var lineSpacing = 0f
    private val symbols: Set<Char> = setOf('。', '？', '！', '，', '、', '；', '：', '）', '》')
    private val fontMetrics: Paint.FontMetrics

    init {
        lineSpacing = lineSpacingExtra
        textPaint.textSize = textSize
        textPaint.color = currentTextColor
        textPaint.typeface = typeface
        textPaint.letterSpacing = letterSpacing
        fontMetrics = textPaint.fontMetrics
    }

    override fun onDraw(canvas: Canvas) {
        lineText.clear()
        lineEndIndex.clear()

        val text = text?.toString() ?: ""
        if (text.isEmpty()) {
            super.onDraw(canvas)
            return
        }

        val x = paddingLeft.toFloat()
        var lineY = paddingTop - fontMetrics.ascent
        val maxWidth = width - paddingRight - paddingLeft
        val textSizePx = textPaint.textSize
        var lineStart = 0
        val lineHeight = fontMetrics.descent - fontMetrics.ascent + lineSpacing

        while (lineStart < text.length) {
            textPaint.letterSpacing = letterSpacing
            val count = textPaint.breakText(
                text, lineStart, text.length, true,
                maxWidth.toFloat(), null
            )
            if (count == 0) break // 防止无字符可绘制时死循环

            val endIndex = lineStart + count
            val remainingText = endIndex < text.length

            when {
                // 处理最后一行
                !remainingText -> {
                    val thisLine = text.substring(lineStart)
                    lineEndIndex.add(text.length)
                    lineText.add(thisLine)
                    canvas.drawText(thisLine, x, lineY, textPaint)
                    lineStart = endIndex
                }

                // 处理换行符
                text[lineStart] == '\n' -> {
                    lineEndIndex.add(lineStart + 1)
                    lineText.add("\n")
                    canvas.drawText("\n", x, lineY, textPaint)
                    lineStart++
                    lineY += lineHeight
                }

                // 行内换行符处理
                text.substring(lineStart, endIndex).contains("\n") -> {
                    val newLinePos = text.indexOf("\n", lineStart)
                    val lineEnd = newLinePos + 1
                    val thisLine = text.substring(lineStart, lineEnd)
                    lineEndIndex.add(lineEnd)
                    lineText.add(thisLine)
                    canvas.drawText(thisLine, x, lineY, textPaint)
                    lineStart = lineEnd
                    lineY += lineHeight
                }

                // 标点符号处理
                endIndex < text.length && symbols.contains(text[endIndex]) -> {
                    val lookahead =
                        if (endIndex + 1 < text.length && symbols.contains(text[endIndex + 1])) 2 else 1
                    val lineEnd = (lineStart + count + lookahead).coerceAtMost(text.length)
                    val thisLine = text.substring(lineStart, lineEnd)
                    lineEndIndex.add(lineEnd)
                    lineText.add(thisLine)
                    adjustLetterSpacingAndDraw(thisLine, maxWidth, textSizePx, x, lineY, canvas)
                    lineStart = lineEnd
                    lineY += lineHeight
                }

                // 普通行处理
                else -> {
                    val thisLine = text.substring(lineStart, endIndex)
                    lineEndIndex.add(endIndex)
                    lineText.add(thisLine)
                    adjustLetterSpacingAndDraw(thisLine, maxWidth, textSizePx, x, lineY, canvas)
                    lineStart = endIndex
                    lineY += lineHeight
                }
            }
        }
    }

    private fun adjustLetterSpacingAndDraw(
        line: String,
        maxWidth: Int,
        textSizePx: Float,
        x: Float,
        y: Float,
        canvas: Canvas
    ) {
        if (line.isEmpty()) return
        val textWidth = textPaint.measureText(line)
        val space = if (line.length > 1) (maxWidth - textWidth) / (line.length - 1) else 0f
        if (space > 0) {
            textPaint.letterSpacing += space / textSizePx
        }
        canvas.drawText(line, x, y, textPaint)
        textPaint.letterSpacing = letterSpacing // 重置字间距
    }

    fun getLineCountCus(): Int = lineText.size

    fun getLineText(line: Int): String = lineText.getOrElse(line) { "" }

    fun getLineEnd(line: Int): Int = lineEndIndex.getOrElse(line) { 0 }
}
