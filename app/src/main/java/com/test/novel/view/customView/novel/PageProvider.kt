package com.test.novel.view.customView.novel

import android.view.View

/**
 * 页面内容提供者接口
 */
interface PageProvider {
    /**
     * 获取总页数
     */
    fun getPageCount(): Int

    /**
     * 绑定页面内容到视图
     * @param pageView 页面视图容器
     * @param pageIndex 页面索引
     */
    fun bindPage(pageView: View, pageIndex: Int)

    /**
     * 添加新页面
     * @param pageContent 页面内容
     * @return 新页面的索引，添加失败则返回-1
     */
    fun addPage(pageContent: String): Int

}