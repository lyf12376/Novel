package com.test.novel.view.customView.novel

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import java.util.LinkedList

/**
 * 页面工厂类，负责创建和回收页面视图
 */
class PageFactory(private val context: Context) {

    // 页面视图缓存池
    private val pageViewPool = LinkedList<View>()

    /**
     * 创建页面视图，优先从缓存池获取，没有则创建新的
     */
    fun createPageView(): View {
        return if (pageViewPool.isEmpty()) {
            // 创建新的页面视图
            FrameLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        } else {
            // 从缓存池取出一个
            pageViewPool.pop()
        }
    }

    /**
     * 回收页面视图到缓存池
     */
    fun recyclePage(pageView: View) {
        // 清除视图状态
        (pageView as FrameLayout).removeAllViews()
        pageView.alpha = 1f
        pageView.translationX = 0f
        pageView.translationY = 0f
        pageView.rotation = 0f
        pageView.scaleX = 1f
        pageView.scaleY = 1f

        // 放入缓存池
        pageViewPool.push(pageView)
    }
}