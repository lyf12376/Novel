package com.test.novel.view.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout


class NovelFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    var clickList: List<() -> Unit> = listOf({}, {}, {})
) : FrameLayout(context, attrs) {
    private var isSingleTap = 0

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {

                }
                MotionEvent.ACTION_MOVE -> {
                    // 如果有移动，则不是单击
                    isSingleTap = -1
                }
                MotionEvent.ACTION_UP -> {
                    // 在UP时，如果仍然认为是单击，则拦截事件
                    isSingleTap ++
                    if (isSingleTap == 1) {
                        val screenWidth = width
                        val x = it.x

                        when {
                            x < screenWidth / 3 -> {
                                // 点击在左侧
                                clickList[0]()
                            }
                            x < 2 * screenWidth / 3 -> {
                                // 点击在中间
                                clickList[1]()
                            }
                            else -> {
                                // 点击在右侧
                                clickList[2]()
                            }
                        }
                        isSingleTap = 0
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("TAG", "onTouchEvent: handle")
        event?.let {
        }
        isSingleTap = 0
        return true
    }
}

