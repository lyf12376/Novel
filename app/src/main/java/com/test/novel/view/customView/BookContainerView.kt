package com.test.novel.view.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class BookContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private var isDeleting: Boolean = false, // 删除模式标志
    private val onDelete: () -> Unit
) : ConstraintLayout(context, attrs) {

    private val longPressTimeout = 500L // 长按时间

    init {
        // 初始化布局和触摸事件
        setOnLongClickListener {
            if (!isDeleting) {
                enterDeleteMode()
            }
            true
        }
    }

    // 添加选择框并进入删除模式
    private fun enterDeleteMode() {
        isDeleting = true
    }


    // 退出删除模式
    fun exitDeleteMode() {
        isDeleting = false
    }

    // 是否处于删除模式
    fun isDeleteMode(): Boolean = isDeleting
}
