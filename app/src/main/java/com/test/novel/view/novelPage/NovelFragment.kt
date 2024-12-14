package com.test.novel.view.novelPage

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.test.novel.R
import com.test.novel.databinding.FragmentNovelBinding
import com.test.novel.model.BookBrief
import com.test.novel.utils.SizeUtils
import com.test.novel.utils.SizeUtils.navigationBarHeight
import com.test.novel.utils.SizeUtils.statusBarHeight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private const val ARG_PARAM1 = "bookBrief"
@AndroidEntryPoint
class NovelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val args: NovelFragmentArgs by navArgs()
    private lateinit var novelFragmentViewModel: NovelFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        novelFragmentViewModel = ViewModelProvider(this)[NovelFragmentViewModel::class.java]
        val bookBrief = Json.decodeFromString(BookBrief.serializer(), args.bookBrief)
        novelFragmentViewModel.sendIntent(BookIntent.Init(bookBrief))
        println(bookBrief.isLocal)
        if (bookBrief.isLocal) {
            novelFragmentViewModel.sendIntent(BookIntent.GetContentFromLocal(bookBrief.bookId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_novel, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNovelBinding.bind(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, statusBarHeight, systemBars.right, 0)
            insets
        }

        val novelFrameLayout = binding.novelFrame
        val adapter = PageFragmentAdapter(
            this,
            novelFragmentViewModel
        )
        binding.novelText.offscreenPageLimit = 3
        binding.novelText.adapter = adapter
        binding.novelText.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                novelFragmentViewModel.sendIntent(BookIntent.SetCurrentIndex(binding.novelText.currentItem))
            }
        })

        novelFrameLayout.apply {
            // 定义点击事件的处理函数
            val leftClick = {
                binding.novelText.setCurrentItem(binding.novelText.currentItem - 1, true)
            }
            val middleClick = {
                novelFragmentViewModel.sendIntent(BookIntent.ShowBar)
            }
            val rightClick = {
                binding.novelText.setCurrentItem(binding.novelText.currentItem + 1, true)
            }
            // 设置点击事件列表
            this.clickList = listOf(leftClick, middleClick, rightClick)
        }
        val topBar = binding.topBar
        val bottomBar = binding.bottomBar
        topBar.visibility = View.GONE
        bottomBar.visibility = View.GONE

        fun animateBars(show: Boolean) {
            if (show) {
                topBar.visibility = View.VISIBLE
                topBar.animate()
                    .translationY(0F)
                    .setDuration(500)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()

                bottomBar.visibility = View.VISIBLE
                bottomBar.animate()
                    .translationY(0F)
                    .setDuration(500)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()

                activity?.let {
                    WindowInsetsControllerCompat(it.window, binding.root).apply {
                        show(WindowInsetsCompat.Type.systemBars())
                    }
                }
            } else {
                topBar.animate()
                    .translationY((-topBar.height).toFloat())
                    .setDuration(500)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction { topBar.visibility = View.GONE }
                    .start()

                bottomBar.animate()
                    .translationY(bottomBar.height.toFloat() + navigationBarHeight)
                    .setDuration(500)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction { topBar.visibility = View.GONE }
                    .start()

                activity?.let {
                    WindowInsetsControllerCompat(it.window, binding.root).apply {
                        hide(WindowInsetsCompat.Type.systemBars())
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                novelFragmentViewModel.state.collect { state ->
                    animateBars(state.showBar)
                }
            }
        }

    }



    companion object {
        @JvmStatic
        fun newInstance(bookBrief: BookBrief) =
            NovelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, Json.encodeToString(BookBrief.serializer(), bookBrief))
                }
            }
    }
}