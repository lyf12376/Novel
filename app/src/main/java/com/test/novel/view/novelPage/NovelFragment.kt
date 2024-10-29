package com.test.novel.view.novelPage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.novel.R
import com.test.novel.databinding.NovelReadBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "novelId"

/**
 * A simple [Fragment] subclass.
 * Use the [NovelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NovelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private val novelFragmentViewModel: NovelFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            param1 = bundle.getInt(ARG_PARAM1)
            param1?.let { novelFragmentViewModel.init(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.novel_read, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var statusBarHeight = 0
        var navigationBarHeight = 0
        val binding = NovelReadBinding.bind(view)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            if (statusBarHeight == 0) {
                statusBarHeight = systemBars.top
            }
            if (navigationBarHeight == 0) {
                navigationBarHeight = systemBars.bottom
            }
            v.setPadding(systemBars.left, statusBarHeight, systemBars.right, systemBars.bottom)
            insets
        }
//        activity?.let {
//            WindowInsetsControllerCompat(it.window, binding.root).apply {
//                hide(WindowInsetsCompat.Type.systemBars())
//            }
//        }
        val novelFrameLayout = binding.novelFrame
        val adapter = PageFragmentAdapter(
            activity as AppCompatActivity,
            listOf("dasddasdasdasdasdas", "dasdsadasdasdasdasdasd", "asdsadsadasdasdasds")
        )
        binding.novelText.adapter = adapter
        novelFrameLayout.apply {
            // 定义点击事件的处理函数
            val leftClick = { binding.novelText.currentItem -= 1 }
            val middleClick = {
                novelFragmentViewModel.showOrHideBar()
            }
            val rightClick = { binding.novelText.currentItem += 1 }
            // 设置点击事件列表
            this.clickList = listOf(leftClick, middleClick, rightClick)
        }
        val topBar = binding.topBar
        val bottomBar = binding.bottomBar
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                novelFragmentViewModel.showBar.collect { showBar ->
                    if (showBar) {
                        topBar.visibility = View.VISIBLE
                        topBar.animate()
                            .translationY(0F)
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .start()
                        bottomBar.animate()
                            .translationY(0F)
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .start()
                        activity?.let {
                            WindowInsetsControllerCompat(it.window, binding.root).apply {
                                show(WindowInsetsCompat.Type.systemBars())
                            }
                        }
                    } else {
                        topBar.animate()
                            .translationY((-topBar.height).toFloat())
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .withEndAction { topBar.visibility = View.GONE }
                            .start()
                        bottomBar.animate()
                            .translationY(bottomBar.height.toFloat() + navigationBarHeight)
                            .setDuration(500) // 修改持续时间为500毫秒
                            .setInterpolator(AccelerateDecelerateInterpolator()) // 使用加速减速插值器
                            .start()
                        activity?.let {
                            WindowInsetsControllerCompat(it.window, binding.root).apply {
                                hide(WindowInsetsCompat.Type.systemBars())
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NovelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NovelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}