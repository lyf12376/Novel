package com.test.novel.view.customView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int, // 列数
    private val horizontalSpacing: Int, // 横向间距
    private val verticalSpacing: Int, // 垂直间距
    private val includeEdge: Boolean // 是否包括边缘间距
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            // 设置横向间距
            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount
            outRect.right = (column + 1) * horizontalSpacing / spanCount

            // 设置垂直间距
            if (position < spanCount) { // 第一行
                outRect.top = verticalSpacing
            }
            outRect.bottom = verticalSpacing // 底部间距
        } else {
            // 设置横向间距
            outRect.left = column * horizontalSpacing / spanCount
            outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / spanCount

            // 设置垂直间距
            if (position >= spanCount) { // 不是第一行
                outRect.top = verticalSpacing
            }
        }
    }
}

