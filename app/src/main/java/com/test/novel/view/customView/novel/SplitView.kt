package com.test.novel.view.customView.novel

import android.content.Context
import android.graphics.Canvas
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


class NewNovelTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val lineTexts = mutableListOf<String>()
    private val lineEndIndices = mutableListOf<Int>()
    private var pageMaxLines = 0

    private val textPaint = TextPaint().apply {
        color = currentTextColor
        textSize = this@NewNovelTextView.textSize
        typeface = this@NewNovelTextView.typeface
    }


    private val symbols: Set<Char> = setOf('。', '？', '！', '，', '、', '；', '：', '’', '”', '）', '》')
    private val fontMetrics = textPaint.fontMetrics
    private val lineHeight: Float by lazy {
        fontMetrics.descent - fontMetrics.ascent + lineSpacingExtra
    }
    private var pages:List<String> = listOf()
    var isDrawEnable = true
        set(value) {
            invalidate()
            field = value
        }
    init {
        // 确保初始值同步
        textPaint.letterSpacing = letterSpacing
    }


    /**
     * 获取当前页能容纳的文本结束位置
     * @return 文本的结束索引位置
     */
    fun getPageEnd(): Int {
        return if (lineEndIndices.isNotEmpty()) lineEndIndices.last() else 0
    }

    /**
     * 重写onDraw方法，支持纯测量模式
     */
    override fun onDraw(canvas: Canvas) {
        if (!isDrawEnable) {
            // 纯测量模式，调用自定义的绘制方法但不实际绘制
            println("withOutDraw")
            measureLayoutWithoutDrawing()
            return
        }
        else
            drawTextLayout(canvas)
    }

    private fun measureLayoutWithoutDrawing() {
        // 这个方法会填充lineTexts和lineEndIndices，但不真正绘制
        // 逻辑与onDraw相同，但没有实际绘制操作

        lineTexts.clear()
        lineEndIndices.clear()

        // 添加页面存储列表
        val pages = mutableListOf<List<String>>()
        val currentPageLines = mutableListOf<String>()
        var currentLineCount = 0

        val currentText = text.toString()
        if (currentText.isEmpty()) return

        val maxWidth = width - paddingLeft - paddingRight
        val textLength = currentText.length
        var lineStart = 0
        var lineY = paddingTop - fontMetrics.ascent

        while (lineStart < textLength) {
            // 保存原始letterSpacing
            val originalSpacing = textPaint.letterSpacing

            // 计算当前行可用字符数
            val count = textPaint.breakText(
                currentText, lineStart, textLength,
                true, maxWidth.toFloat(), null
            )
            val endIndex = (lineStart + count).coerceAtMost(textLength)

            when {
                // 处理最后一行
                lineStart + count >= textLength -> handleLastLine(currentText, lineStart, endIndex)

                // 处理空行
                currentText[lineStart] == '\n' -> handleEmptyLine(lineStart)

                // 处理显式换行符
                else -> {
                    val newLinePos = currentText.indexOf('\n', lineStart)
                    when {
                        newLinePos in lineStart until endIndex ->
                            handleNewLine(currentText, lineStart, newLinePos)

                        shouldAdjustForSymbol(currentText, endIndex) ->
                            handleSymbolStartLine(currentText, lineStart, endIndex, maxWidth)

                        else -> handleNormalLine(currentText, lineStart, endIndex, maxWidth)
                    }
                }
            }

            // 更新位置
            lineY += lineHeight
            lineStart = lineEndIndices.last()
            // 恢复letterSpacing
            textPaint.letterSpacing = originalSpacing

            // 计算页面分割
            if (lineY > height - paddingBottom && pageMaxLines == 0) {
                pageMaxLines = lineTexts.size
            }

            // 添加当前行到当前页中
            if (lineTexts.isNotEmpty()) {
                currentPageLines.add(lineTexts.last())
                currentLineCount++
                println("$pageMaxLines $currentLineCount${pageMaxLines in 1..currentLineCount}")
                // 如果达到每页最大行数或者是最后一行，添加当前页到pages列表
                if ((pageMaxLines in 1..currentLineCount) || lineStart >= textLength) {
                    println("currentLineCount $currentLineCount")
                    pages.add(currentPageLines.toList())
                    currentPageLines.clear()
                    currentLineCount = 0
                }
            }
        }

        // 确保最后一页被添加（如果有内容但未达到pageMaxLines）
        if (currentPageLines.isNotEmpty()) {
            pages.add(currentPageLines.toList())
        }


        this.pages = pages.map {
            it.joinToString("")
        }


    }

    private fun drawTextLayout(canvas: Canvas){
        // 每次绘制前清空数据
        lineTexts.clear()
        lineEndIndices.clear()

        val currentText = text.toString()
        if (currentText.isEmpty()) return

        val maxWidth = width - paddingLeft - paddingRight
        val textLength = currentText.length
        var lineStart = 0
        var lineY = paddingTop - fontMetrics.ascent

        while (lineStart < textLength) {
            // 保存原始letterSpacing
            val originalSpacing = textPaint.letterSpacing

            // 计算当前行可用字符数
            val count = textPaint.breakText(
                currentText, lineStart, textLength,
                true, maxWidth.toFloat(), null
            )
            val endIndex = (lineStart + count).coerceAtMost(textLength)

            when {
                // 处理最后一行
                lineStart + count >= textLength -> handleLastLine(currentText, lineStart, endIndex)

                // 处理空行
                currentText[lineStart] == '\n' -> handleEmptyLine(lineStart)

                // 处理显式换行符
                else -> {
                    val newLinePos = currentText.indexOf('\n', lineStart)
                    when {
                        newLinePos in lineStart until endIndex ->
                            handleNewLine(currentText, lineStart, newLinePos)

                        shouldAdjustForSymbol(currentText, endIndex) ->
                            handleSymbolStartLine(currentText, lineStart, endIndex, maxWidth)

                        else -> handleNormalLine(currentText, lineStart, endIndex, maxWidth)
                    }
                }
            }

            // 绘制当前行并更新位置
            drawLine(canvas, lineTexts.last(), paddingLeft.toFloat(), lineY)
            lineY += lineHeight
            lineStart = lineEndIndices.last()
            // 恢复letterSpacing
            textPaint.letterSpacing = originalSpacing

            if (lineY>height - paddingBottom && isDrawEnable) {
                println(lineTexts.size)
                pageMaxLines = lineTexts.size
                break
            }

        }
    }


    private fun handleLastLine(text: String, start: Int, end: Int) {
        lineTexts.add(text.substring(start, end))
        lineEndIndices.add(end)
    }

    private fun handleEmptyLine(start: Int) {
        lineTexts.add("\n")
        lineEndIndices.add(start + 1)
    }

    private fun handleNewLine(text: String, start: Int, newLinePos: Int) {
        lineTexts.add(text.substring(start, newLinePos + 1))
        lineEndIndices.add(newLinePos + 1)
    }

    private fun shouldAdjustForSymbol(text: String, endIndex: Int): Boolean {
        return endIndex < text.length && symbols.contains(text[endIndex])
    }

    private fun handleSymbolStartLine(
        text: String,
        start: Int,
        end: Int,
        maxWidth: Int
    ) {
        // 处理连续标点情况
        val adjust = if (end + 1 < text.length && symbols.contains(text[end + 1])) 2 else 1
        val lineEnd = (start + (end - start) + adjust).coerceAtMost(text.length)

        lineTexts.add(text.substring(start, lineEnd))
        lineEndIndices.add(lineEnd)
        adjustLetterSpacing(lineTexts.last(), maxWidth)
    }

    private fun handleNormalLine(
        text: String,
        start: Int,
        end: Int,
        maxWidth: Int
    ) {
        lineTexts.add(text.substring(start, end))
        lineEndIndices.add(end)
        adjustLetterSpacing(lineTexts.last(), maxWidth)
    }

    /* 绘制相关方法 */

    private fun drawLine(
        canvas: Canvas,
        text: String,
        x: Float,
        lineY: Float
    ) {
        canvas.drawText(text, x, lineY, textPaint)
    }

    private fun adjustLetterSpacing(text: String, maxWidth: Int) {
        if (text.length <= 1) return
        val textWidth = textPaint.measureText(text)
        val space = (maxWidth - textWidth) / (text.length - 1)
        textPaint.letterSpacing += space / textPaint.textSize
    }

    fun getLineCountCus() = lineTexts.size

    fun getLineText(line: Int) = lineTexts[line]

    fun getPages():List<String>{
        println(pages)
        return pages
    }
}
