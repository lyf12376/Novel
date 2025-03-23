package com.test.novel.view.newNovelPage

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.databinding.FragmentReadBinding
import com.test.novel.model.BookBrief
import com.test.novel.utils.SizeUtils.navigationBarHeight
import com.test.novel.utils.SizeUtils.statusBarHeight
import com.test.novel.view.customView.novel.PageTurnListener
import com.test.novel.view.customView.novel.PageTurnView
import com.test.novel.view.customView.novel.PageType
import com.test.novel.view.customView.novel.ReadPageProvider
import com.test.novel.view.novelPage.NovelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private const val BOOK_BRIEF = "bookBrief"
@AndroidEntryPoint
class ReadFragment : Fragment() {
    private lateinit var binding :FragmentReadBinding
    private lateinit var bookBrief: BookBrief
    private lateinit var pageProvider: ReadPageProvider
    private lateinit var pageTurnView: PageTurnView
    private val viewModel: ReadViewModel by viewModels()
    private lateinit var toolBinding: FragmentPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookBrief = Json.decodeFromString(BookBrief.serializer(), it.getString(BOOK_BRIEF).toString())
        }
        pageProvider = ReadPageProvider()
        viewModel.loadChapter(bookBrief.bookId)
        toolBinding = FragmentPageBinding.inflate(
            LayoutInflater.from(requireContext().applicationContext),
            null,
            false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_read, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReadBinding.bind(view)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, statusBarHeight, systemBars.right, 0)
            insets
        }
        pageTurnView = binding.page

        pageTurnView.pageProvider = pageProvider
        pageTurnView.pageListener = object : PageTurnListener{
            override fun onPageChanged(pageIndex: Int) {

            }

            override fun onCenterClick() {
                viewModel.showOrHideBar()
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.showBar.collect {
                    animateBars(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.chapters.filter {
                    it.isNotEmpty()
                }.collect {
                    paginateText(it[0].content)
                }
            }
        }
    }


    private fun paginateText(text: String) {
        pageTurnView.post {
            val width = pageTurnView.measuredWidth
            val height = pageTurnView.measuredHeight

            toolBinding.root.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            )
            toolBinding.root.layout(0, 0, width, height)
            toolBinding.root.draw(Canvas())
            val toolNovelText = toolBinding.novelText
            // 3. 作为工具使用
            toolNovelText.isDrawEnable = false
            toolNovelText.text = text
            toolNovelText.draw(Canvas())

            val pages = toolNovelText.getPages()
            pages.forEach { page ->
                pageTurnView.appendPage(PageType.NormalPage(page), false)
            }
//            // 4. 释放资源
//            toolBinding.root.removeAllViews()
        }
    }

    private fun animateBars(show: Boolean) {
        val topBar = binding.topBar
        val bottomBar = binding.bottomBar

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
                .withEndAction { bottomBar.visibility = View.GONE }
                .start()

            activity?.let {
                WindowInsetsControllerCompat(it.window, binding.root).apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(bookBrief: BookBrief) =
            ReadFragment().apply {
                arguments = Bundle().apply {
                    putString(BOOK_BRIEF, Json.encodeToString(BookBrief.serializer(), bookBrief))
                }
            }
    }
}