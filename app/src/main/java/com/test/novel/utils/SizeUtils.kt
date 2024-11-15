package com.test.novel.utils

import android.graphics.Rect
import android.util.TypedValue
import android.widget.TextView
import com.test.novel.view.customView.NovelTextView

object SizeUtils {
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
        /*
        * The first row's height is different from other row.
        */
        val h = view.bottom - view.top - view.paddingTop - view.paddingBottom
        val firstH = getLineHeight(0, view)
        val otherH = getLineHeight(1, view)
        println("firstH: $firstH otherH: $otherH h: $h")
        val lineHeight = view.paint.fontMetricsInt.bottom - view.paint.fontMetricsInt.top
        println(lineHeight)
        var lines = 1
        if (otherH != 0) lines = (h - firstH + (otherH - lineHeight)) / otherH + 1 //仅一行时返回1
        return lines
    }

    //获取某一行的行高
    private fun getLineHeight(line: Int, view: TextView): Int {
        val rect = Rect()
        view.getLineBounds(line, rect)
        return rect.bottom - rect.top
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