package com.test.novel.view.customView.novel

/**
 * 页面翻转监听接口
 */
interface PageTurnListener {
    /**
     * 页面变化回调
     * @param pageIndex 新的页面索引
     */
    fun onPageChanged(pageIndex: Int)

    /**
     * 中间区域点击回调
     */
    fun onCenterClick()
}