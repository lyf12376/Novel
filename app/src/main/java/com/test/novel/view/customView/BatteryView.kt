package com.test.novel.view.customView



import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.test.novel.R

import kotlin.math.min

class BatteryView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Default values
    private var borderColor = Color.BLACK
    private var progressColor = Color.GREEN
    private var batteryLevel = 50 // 0-100
    private var borderWidth = 5f
    private var headHeight = 0f
    private var headWidth = 0f

    // Paints
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val headPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // Drawing areas
    private val borderRect = RectF()
    private val progressRect = RectF()
    private val headRect = RectF()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BatteryView)

        try {
            borderColor = typedArray.getColor(R.styleable.BatteryView_borderColor, Color.BLACK)
            progressColor = typedArray.getColor(R.styleable.BatteryView_progressColor, Color.GREEN)
            batteryLevel = typedArray.getInteger(R.styleable.BatteryView_batteryLevel, 50)
            borderWidth = typedArray.getDimension(R.styleable.BatteryView_borderWidth, 5f)
            headHeight = typedArray.getDimension(R.styleable.BatteryView_headHeight, 0f)
            headWidth = typedArray.getDimension(R.styleable.BatteryView_headWidth, 0f)
        } finally {
            typedArray.recycle()
        }

        updatePaints()
    }

    private fun updatePaints() {
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth

        progressPaint.color = progressColor

        headPaint.color = borderColor
    }

    /**
     * 设置边框颜色
     */
    fun setBorderColor(color: Int) {
        borderColor = color
        updatePaints()
        invalidate()
    }

    /**
     * 设置进度颜色
     */
    fun setProgressColor(color: Int) {
        progressColor = color
        updatePaints()
        invalidate()
    }

    /**
     * 设置电量
     */
    fun setBatteryLevel(level: Int) {
        batteryLevel = level.coerceIn(0, 100)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 100
        val desiredHeight = 50

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 计算电池的实际宽度和高度
        val actualHeadWidth = if (headWidth <= 0) h / 6f else headWidth
        val actualHeadHeight = if (headHeight <= 0) h / 3f else headHeight

        // 电池边框
        borderRect.set(
            borderWidth / 2,
            borderWidth / 2,
            w - actualHeadWidth - borderWidth / 2,
            h - borderWidth / 2
        )

        // 电池头部
        headRect.set(
            w - actualHeadWidth,
            (h - actualHeadHeight) / 2,
            w.toFloat(),
            (h + actualHeadHeight) / 2
        )

        // 电池电量
        progressRect.set(
            borderRect.left + borderWidth,
            borderRect.top + borderWidth,
            borderRect.right - borderWidth,
            borderRect.bottom - borderWidth
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制电池边框
        canvas.drawRoundRect(borderRect, 4f, 4f, borderPaint)

        // 绘制电池头部
        canvas.drawRect(headRect, headPaint)

        // 计算电量进度
        val progressWidth = (progressRect.width() * batteryLevel / 100)
        val progressRight = progressRect.left + progressWidth

        // 绘制电量进度
        canvas.drawRoundRect(
            progressRect.left,
            progressRect.top,
            progressRight,
            progressRect.bottom,
            2f, 2f,
            progressPaint
        )
    }
}