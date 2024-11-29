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
//        println(y)
//        if (text != "")
//            println(text.substring(0,8))
        val x = paddingLeft.toFloat()
        var lineY = paddingTop - textPaint.fontMetrics.ascent
        val text = text.toString()
        val maxWidth = width - paddingRight - paddingLeft
        val textSizePx = textPaint.textSize
        val letterSpacePx = textSizePx * textPaint.letterSpacing
        var lineStart = 0
        while (lineStart < text.length) {
            textPaint.letterSpacing = letterSpacing
            val count = textPaint.breakText(
                text, lineStart, text.length, true,
                maxWidth.toFloat(), null
            )
            if (lineStart + count == text.length) {
                val thisLine = text.substring(lineStart, lineStart + count)
                lineEndIndex.add(lineStart + count)
                lineText.add(thisLine)
                canvas.drawText(thisLine, x, lineY, textPaint)
                break
            }
            else if (text.substring(lineStart, lineStart + count).contains("\n")) {
                val lineEnd = lineStart + text.substring(lineStart, lineStart + count).indexOf("\n") + 1
                val thisLine = text.substring(lineStart, lineEnd)
                lineEndIndex.add(lineEnd)
                lineText.add(thisLine)
                canvas.drawText(thisLine, x, lineY, textPaint)
                lineStart = lineEnd
                lineY += textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent + lineSpacing
            } else if (run { try {
                    symbols.contains(text[lineStart + count])
                } catch (e: Exception) {
                    false
                } }){
                //让标点符号能够显示在同一行
                val lineEnd = lineStart + count + 1
                if (try {
                        text[lineEnd].toString() == "\n"
                }catch (e:Exception){
                    false
                }){
                    val thisLine = text.substring(lineStart, lineEnd)
                    println(thisLine)
                    lineEndIndex.add(lineEnd + 1)
                    lineText.add(thisLine)
                    val textWidth = textPaint.measureText(thisLine)
                    val space = (maxWidth - textWidth) / (thisLine.length - 1)
                    textPaint.letterSpacing += space / textSizePx
                    canvas.drawText(thisLine, x, lineY, textPaint)
                    lineStart = lineEnd + 1
                    lineY += textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent + lineSpacing
                }
                else {
                    val thisLine = text.substring(lineStart, lineEnd)
                    lineEndIndex.add(lineEnd)
                    lineText.add(thisLine)
                    val textWidth = textPaint.measureText(thisLine)
                    val space = (maxWidth - textWidth) / (thisLine.length - 1)
                    textPaint.letterSpacing += space / textSizePx
                    canvas.drawText(thisLine, x, lineY, textPaint)
                    lineStart = lineEnd
                    lineY += textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent + lineSpacing
                }
            }else{
                val lineEnd = lineStart + count
                if (text[lineEnd] == '\n'){
                    val thisLine = text.substring(lineStart, lineEnd)
                    lineText.add(thisLine)
                    lineEndIndex.add(lineEnd + 1)
                    val textWidth = textPaint.measureText(thisLine)
                    val space = (maxWidth - textWidth) / (thisLine.length - 1)
                    textPaint.letterSpacing += space / textSizePx
                    canvas.drawText(text.substring(lineStart, lineEnd), x, lineY, textPaint)
                    lineStart = lineEnd + 1
                    lineY += textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent + lineSpacing
                }
                else {
                    val thisLine = text.substring(lineStart, lineEnd)
                    lineText.add(thisLine)
                    lineEndIndex.add(lineEnd)
                    val textWidth = textPaint.measureText(thisLine)
                    val space = (maxWidth - textWidth) / (thisLine.length - 1)
                    textPaint.letterSpacing += space / textSizePx
                    canvas.drawText(text.substring(lineStart, lineEnd), x, lineY, textPaint)
                    lineStart = lineEnd
                    lineY += textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent + lineSpacing
                }
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
