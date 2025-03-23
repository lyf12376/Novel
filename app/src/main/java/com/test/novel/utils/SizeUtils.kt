package com.test.novel.utils

import android.graphics.Rect
import android.util.TypedValue
import android.widget.TextView
import com.bumptech.glide.Glide
import com.test.novel.view.customView.NovelTextView

object SizeUtils {
    var navigationBarHeight = 0
    var statusBarHeight = 0

    fun dp2px(dp: Float): Int {
        val scale = AppUtils.context.resources.displayMetrics.density
        return (dp * scale).toInt()
    }

    fun px2dp(px: Int): Float {
        val scale = AppUtils.context.resources.displayMetrics.density
        return px / scale
    }

    fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, AppUtils.context.resources.displayMetrics)
    }

    fun px2sp(px: Int): Float {
        return px / AppUtils.context.resources.displayMetrics.scaledDensity
    }

    fun getPageLineCount(view: TextView): Int {
        val h = view.height - view.paddingTop - view.paddingBottom
        if (h <= 0) return 0

        val firstH = getLineHeight(0, view)
        if (firstH <= 0) return 1

        val otherH = if (view.lineCount > 1) getLineHeight(1, view) else firstH
        val lineSpacing = view.lineSpacingExtra + view.lineSpacingMultiplier * view.textSize

        // Calculate how many lines can fit
        if (otherH <= 0) return 1

        // Account for first line being potentially different height
        var lines = 1
        val remainingHeight = h - firstH
        if (remainingHeight > 0) {
            lines += (remainingHeight / otherH).toInt()
        }
        return lines.coerceAtLeast(1)
    }

    private fun getLineHeight(line: Int, view: TextView): Int {
        if (view.lineCount <= line) return 0
        val rect = Rect()
        view.getLineBounds(line, rect)
        return rect.height()
    }

    fun getPageLineCountCustom(view: NovelTextView): Int {
        /*
        * The first row's height is different from other row.
        */
        val h = view.bottom - view.top - view.paddingTop - view.paddingBottom
        val firstH = getLineHeightCustom(0, view)
        val otherH = getLineHeightCustom(1, view)
        println("firstH: $firstH otherH: $otherH h: $h")
        val lineHeight = view.paint.fontMetricsInt.bottom - view.paint.fontMetricsInt.top
        println(lineHeight)
        var lines = 1
        if (otherH != 0) lines = (h - firstH + (otherH - lineHeight)) / otherH + 1 //仅一行时返回1
        return lines
    }

    //获取某一行的行高
    private fun getLineHeightCustom(line: Int, view: TextView): Int {
        val rect = Rect()
        view.getLineBounds(line, rect)
        return rect.bottom - rect.top
    }
}