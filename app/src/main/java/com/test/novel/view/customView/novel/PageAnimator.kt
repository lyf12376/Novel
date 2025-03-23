package com.test.novel.view.customView.novel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

/**
 * 页面动画基础接口
 */
abstract class PageAnimator(protected val pageTurnView: PageTurnView) {
    // 动画插值器
    protected val myInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    // 动画控制器
    protected var animator: ValueAnimator? = null
    // 页面视图的引用
    protected val previousPage: View?
        get() = pageTurnView.getPreviousPage()

    protected val currentPage: View?
        get() = pageTurnView.getCurrentPage()

    protected val nextPage: View?
        get() = pageTurnView.getNextPage()

    // 页面初始化
    open fun onPageLoaded() {
        // 初始化页面状态
        previousPage?.visibility = View.VISIBLE
        currentPage?.visibility = View.VISIBLE
        nextPage?.visibility = View.VISIBLE
    }

    // 页面布局变化
    open fun onLayout() {}

    // 开始翻页
    open fun onFlipStart(direction: Int) {
        if (direction > 0) {
            // 向右翻，准备显示上一页
            previousPage?.visibility = View.VISIBLE
        } else {
            // 向左翻，准备显示下一页
            nextPage?.visibility = View.VISIBLE
        }
    }

    // 翻页过程
    abstract fun onFlipProgress(direction: Int, progress: Float, distance: Float)

    // 翻页结束
    abstract fun onFlipEnd(direction: Int, completed: Boolean, callback: () -> Unit)

    // 直接翻页动画
    abstract fun flipNextPage(callback: () -> Unit)

    abstract fun flipPreviousPage(callback: () -> Unit)

    // 自定义绘制
    open fun onDraw(canvas: Canvas) {}

    // 清除动画状态
    open fun clearAnimation() {
        animator?.cancel()
        animator = null
    }
}

/**
 * 覆盖效果的翻页动画
 * 前一页滑入覆盖当前页，或当前页滑出露出下一页
 */
class CoverPageAnimator(pageTurnView: PageTurnView) : PageAnimator(pageTurnView) {

    override fun onLayout() {
        super.onLayout()
        val width = pageTurnView.width
        val height = pageTurnView.height

        // 上一页初始位于屏幕左侧外
        previousPage?.apply {
            layout(-width, 0, 0, height)
            translationX = 0f  // 不要使用translationX，使用layout定位
            elevation = 20f    // 最上层
        }

        // 当前页正常显示
        currentPage?.apply {
            layout(0, 0, width, height)
            translationX = 0f
            elevation = 10f    // 中间层
        }

        // 下一页与当前页重叠，在底层
        nextPage?.apply {
            layout(0, 0, width, height)
            translationX = 0f
            elevation = 5f     // 最下层
        }
    }

    override fun onPageLoaded() {
        super.onPageLoaded()

        previousPage?.visibility = View.VISIBLE
        currentPage?.visibility = View.VISIBLE
        nextPage?.visibility = View.VISIBLE
    }

    override fun onFlipProgress(direction: Int, progress: Float, distance: Float) {
        if (direction > 0) {
            // 向右翻，当前页保持不动，前一页从左侧进入覆盖
            previousPage?.translationX = distance
        } else {
            // 向左翻，当前页向左移出，下一页保持不动（已在当前页下方）
            currentPage?.translationX = distance.coerceAtMost(0f)
        }
    }

    override fun onFlipEnd(direction: Int, completed: Boolean, callback: () -> Unit) {
        // 取消之前的动画
        clearAnimation()
        val width = pageTurnView.width
        Log.d("TAG", "onFlipEnd: $completed")
        if (completed) {
            // 翻页完成
            animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 250
                interpolator = myInterpolator

                if (direction > 0) {
                    // 向右翻完成：前一页继续向右移动覆盖当前页
                    val startTranslation = previousPage?.translationX ?: 0f
                    val endTranslation = width.toFloat()

                    addUpdateListener { animation ->
                        val fraction = animation.animatedValue as Float
                        previousPage?.translationX = startTranslation + (endTranslation - startTranslation) * fraction
                    }
                } else {
                    // 向左翻完成：当前页继续向左移出屏幕
                    val startTranslation = currentPage?.translationX ?: 0f
                    val endTranslation = -width.toFloat()

                    addUpdateListener { animation ->
                        val fraction = animation.animatedValue as Float
                        currentPage?.translationX = startTranslation + (endTranslation - startTranslation) * fraction
                    }
                }

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        callback()
                    }
                })

                start()
            }
        } else {
            // 取消翻页，恢复原状
            animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 200
                interpolator = myInterpolator

                if (direction > 0) {
                    // 取消向右翻：前一页回到左侧屏幕外
                    val startTranslation = previousPage?.translationX ?: 0f

                    addUpdateListener { animation ->
                        val fraction = animation.animatedValue as Float
                        previousPage?.translationX = startTranslation * (1 - fraction)
                    }
                } else {
                    // 取消向左翻：当前页回到原位
                    val startTranslation = currentPage?.translationX ?: 0f

                    addUpdateListener { animation ->
                        val fraction = animation.animatedValue as Float
                        currentPage?.translationX = startTranslation * (1 - fraction)
                    }
                }

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        callback()
                    }
                })

                start()
            }
        }
    }

    override fun flipNextPage(callback: () -> Unit) {
        animator = ValueAnimator.ofFloat(0f,1f).apply {
            duration = 200
            interpolator = myInterpolator
            addUpdateListener { animation ->
                val fraction = animation.animatedValue as Float
                currentPage?.translationX = -pageTurnView.width * fraction
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    callback()
                }
            })
            start()
        }
    }

    override fun flipPreviousPage(callback: () -> Unit) {
        animator = ValueAnimator.ofFloat(0f,1f).apply {
            duration = 200
            interpolator = myInterpolator
            addUpdateListener { animation ->
                val fraction = animation.animatedValue as Float
                previousPage?.translationX = pageTurnView.width * fraction
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    callback()
                }
            })
            start()
        }
    }

    override fun clearAnimation() {
        super.clearAnimation()
        // 重置动画状态
        animator?.cancel()
        animator = null
    }
}

/**
 * 仿真翻页效果动画
 */
class SimulationPageAnimator(pageTurnView: PageTurnView) : PageAnimator(pageTurnView) {

    private val camera = Camera()
    private val matrix = Matrix()

    override fun onFlipProgress(direction: Int, progress: Float, distance: Float) {
        val width = pageTurnView.width

        if (direction > 0) {
            // 向右翻，模拟书页右侧边缘旋转
            currentPage?.let {
                it.pivotX = 0f
                it.pivotY = it.height / 2f
                it.rotationY = -progress * 90
            }

            previousPage?.let {
                it.pivotX = width.toFloat()
                it.pivotY = it.height / 2f
                it.rotationY = 90 - progress * 90
                it.visibility = View.VISIBLE
            }
        } else {
            // 向左翻，模拟书页左侧边缘旋转
            currentPage?.let {
                it.pivotX = width.toFloat()
                it.pivotY = it.height / 2f
                it.rotationY = progress * 90
            }

            nextPage?.let {
                it.pivotX = 0f
                it.pivotY = it.height / 2f
                it.rotationY = -90 + progress * 90
                it.visibility = View.VISIBLE
            }
        }
    }

    override fun onFlipEnd(direction: Int, completed: Boolean, callback: () -> Unit) {
        val width = pageTurnView.width

        if (completed) {
            // 翻页完成
            if (direction > 0) {
                // 向右翻到上一页
                currentPage?.animate()
                    ?.rotationY(-180f)
                    ?.setDuration(200)
                    ?.withEndAction {
                        callback()
                    }
                    ?.start()

                previousPage?.animate()
                    ?.rotationY(0f)
                    ?.setDuration(200)
                    ?.start()
            } else {
                // 向左翻到下一页
                currentPage?.animate()
                    ?.rotationY(180f)
                    ?.setDuration(200)
                    ?.start()

                nextPage?.animate()
                    ?.rotationY(0f)
                    ?.setDuration(200)
                    ?.withEndAction {
                        callback()
                    }
                    ?.start()
            }
        } else {
            // 取消翻页，恢复原状
            currentPage?.animate()
                ?.rotationY(0f)
                ?.setDuration(200)
                ?.start()

            if (direction > 0) {
                previousPage?.animate()
                    ?.rotationY(90f)
                    ?.setDuration(200)
                    ?.withEndAction {
                        previousPage?.visibility = View.INVISIBLE
                        callback()
                    }
                    ?.start()
            } else {
                nextPage?.animate()
                    ?.rotationY(-90f)
                    ?.setDuration(200)
                    ?.withEndAction {
                        nextPage?.visibility = View.INVISIBLE
                        callback()
                    }
                    ?.start()
            }
        }
    }

    override fun flipNextPage(callback: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun flipPreviousPage(callback: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onPageLoaded() {
        super.onPageLoaded()

        // 重置3D变换
        previousPage?.rotationY = 90f
        currentPage?.rotationY = 0f
        nextPage?.rotationY = -90f
    }

    override fun onLayout() {
        super.onLayout()
        pageTurnView.cameraDistance = pageTurnView.width * 10f
    }
}

/**
 * 平移翻页动画
 * 所有页面在同一平面上水平排列，像轮播图一样滑动切换
 */
class TranslationPageAnimator(pageTurnView: PageTurnView) : PageAnimator(pageTurnView) {

    override fun onLayout() {
        super.onLayout()
        val width = pageTurnView.width
        val height = pageTurnView.height

        // 所有页面都在同一平面上 (相同elevation)
        // 上一页位于左侧
        previousPage?.apply {
            layout(-width, 0, 0, height)
            translationX = 0f
            elevation = 0f
            visibility = View.VISIBLE
        }

        // 当前页在中间
        currentPage?.apply {
            layout(0, 0, width, height)
            translationX = 0f
            elevation = 0f
            visibility = View.VISIBLE
        }

        // 下一页在右侧
        nextPage?.apply {
            layout(width, 0, 2 * width, height)
            translationX = 0f
            elevation = 0f
            visibility = View.VISIBLE
        }
    }

    override fun onPageLoaded() {
        // 在平移动画中，所有页面默认都是可见的
        previousPage?.visibility = View.VISIBLE
        currentPage?.visibility = View.VISIBLE
        nextPage?.visibility = View.VISIBLE
    }

    override fun onFlipProgress(direction: Int, progress: Float, distance: Float) {
        // 所有页面一起移动，保持相对位置
        currentPage?.translationX = distance
        previousPage?.translationX = distance
        nextPage?.translationX = distance
    }

    override fun onFlipEnd(direction: Int, completed: Boolean, callback: () -> Unit) {
        // 取消可能正在进行的动画
        clearAnimation()

        val width = pageTurnView.width

        // 获取当前的位置作为起始点
        val currentTranslationX = currentPage?.translationX ?: 0f

        animator = ValueAnimator.ofFloat(currentTranslationX,
            if (completed) {
                if (direction > 0) width.toFloat() else -width.toFloat()
            } else {
                0f
            }
        ).apply {
            duration = 200
            interpolator = myInterpolator

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float

                // 移动所有页面，保持相对位置
                currentPage?.translationX = value
                previousPage?.translationX = value
                nextPage?.translationX = value
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (!completed) {
                        // 如果是取消翻页，则回到初始状态后直接调用回调
                        callback()
                    } else {
                        // 如果是完成翻页，则在回调之前先确保所有页面平滑过渡
                        // 动画结束时让系统去更新页面状态
                        callback()
                    }
                }
            })
            start()
        }
    }

    override fun flipNextPage(callback: () -> Unit) {
        val width = pageTurnView.width
        animator = ValueAnimator.ofFloat(0f,-width.toFloat()).apply {
            duration = 200
            interpolator = myInterpolator
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float

                // 移动所有页面，保持相对位置
                currentPage?.translationX = value
                previousPage?.translationX = value
                nextPage?.translationX = value
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    callback()
                }
            })
            start()
        }
    }

    override fun flipPreviousPage(callback: () -> Unit) {
        val width = pageTurnView.width
        animator = ValueAnimator.ofFloat(0f,width.toFloat()).apply {
            duration = 200
            interpolator = myInterpolator
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                // 移动所有页面，保持相对位置
                currentPage?.translationX = value
                previousPage?.translationX = value
                nextPage?.translationX = value
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    callback()
                }
            })
            start()
        }
    }

    override fun clearAnimation() {
        super.clearAnimation()

        // 取消所有页面上可能存在的动画
        previousPage?.animate()?.cancel()
        currentPage?.animate()?.cancel()
        nextPage?.animate()?.cancel()
    }
}