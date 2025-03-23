package com.test.novel.view.novelPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.test.novel.R
import com.test.novel.databinding.FragmentPageBinding
import com.test.novel.utils.SizeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TITLE = "title"
private const val TEXT = "text"
private const val LOAD = "load"
private const val INDEX = "pageIndex"
private const val CHAPTER = "chapterIndex"

class PageFragment : Fragment() {
    private var text: String? = ""
    private var load: Boolean = false
    private var title: String? = ""
    private var pageIndex: Int = 0
    private var chapterIndex: Int = 0
    private lateinit var sharedViewModel: NovelFragmentViewModel
    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel =
            ViewModelProvider(requireParentFragment())[NovelFragmentViewModel::class.java]
        arguments?.let {
            title = it.getString(TITLE)
            text = it.getString(TEXT)
            load = it.getBoolean(LOAD)
            pageIndex = it.getInt(INDEX)
            chapterIndex = it.getInt(CHAPTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val novelText = binding.novelText
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.state.collectLatest { state ->
                    if (pageIndex >= state.pages.size) return@collectLatest

                    val page = state.pages[pageIndex]
                    load = page.load
                    title = page.title
                    chapterIndex = page.chapterIndex

                    if (page.text != text) {
                        text = page.text
                        binding.title.text = title
                        novelText.text = text
                    }

                    if (!page.showTitle) {
                        binding.title.visibility = View.GONE
                        constraintSet.connect(binding.novelText.id, ConstraintSet.TOP, binding.topic.id, ConstraintSet.BOTTOM)
                        constraintSet.applyTo(binding.root)
                    } else {
                        binding.title.visibility = View.VISIBLE
                        binding.title.post {
                            binding.title.text = title
                        }
                    }

                    binding.novelChapter.text = "第${chapterIndex}章"
                    binding.topicBack.setOnClickListener {
                        requireParentFragment().findNavController().popBackStack()
                    }

                    // Only process pagination if not already loaded
                    if (!load) {
                        novelText.post {
//                            paginateText()
                        }
                    }
                }
            }
        }
    }

//    private fun paginateText() {
//        val example = text ?: return
//        val novelText = binding.novelText
//
//        // Set the full text to calculate line metrics
//        novelText.text = example
//
//        val pageLines = SizeUtils.getPageLineCount(novelText)
//        val maxLines = novelText.getLineCountCus()
//
//        // If text fits on a single page, no need to paginate
//        if (maxLines <= pageLines) {
//            return
//        }
//
//        // Calculate first page content
//        val lineEndOffset = novelText.getLineEnd(pageLines - 1)
//        if (lineEndOffset <= 0 || lineEndOffset > example.length) return
//
//        val textShowInPage = example.substring(0, lineEndOffset)
//        novelText.text = textShowInPage
//
//        // Calculate additional pages
//        val titleLineCount = SizeUtils.getPageLineCount(binding.title)
//        val effectivePageLines = pageLines - (if (title.isNullOrEmpty()) 0 else titleLineCount)
//
//        val pageList = mutableListOf<PageState>()
//        pageList.add(
//            PageState(
//                chapterIndex = chapterIndex,
//                showTitle = true,
//                title = title ?: "",
//                text = textShowInPage,
//                load = true
//            )
//        )
//
//        var currentOffset = lineEndOffset
//        while (currentOffset < example.length) {
//            // Set remaining text to calculate next page
//            novelText.text = example.substring(currentOffset)
//
//            // Calculate how many lines will fit on next page
//            val nextPageLines = if (pageList.size == 1) effectivePageLines else pageLines
//
//            if (novelText.getLineCountCus() <= nextPageLines) {
//                // All remaining text fits on one page
//                pageList.add(
//                    PageState(
//                        chapterIndex = chapterIndex,
//                        showTitle = false,
//                        title = title ?: "",
//                        text = example.substring(currentOffset),
//                        load = true
//                    )
//                )
//                break
//            } else {
//                // Calculate text for next page
//                val nextLineEndOffset = novelText.getLineEnd(nextPageLines - 1)
//                if (nextLineEndOffset <= 0) break // Safety check
//
//                val nextPageText = example.substring(currentOffset, currentOffset + nextLineEndOffset)
//                pageList.add(
//                    PageState(
//                        chapterIndex = chapterIndex,
//                        showTitle = false,
//                        title = title ?: "",
//                        text = nextPageText,
//                        load = true
//                    )
//                )
//                currentOffset += nextLineEndOffset
//            }
//        }
//
//        // Restore current page text
//        novelText.text = textShowInPage
//
//        // Update ViewModel with new pages
//        sharedViewModel.sendIntent(BookIntent.AddPages(pageList))
//    }

    companion object {
        @JvmStatic
        fun newInstance(pageState: PageState, pageIndex: Int) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, pageState.title)
                    putString(TEXT, pageState.text)
                    putBoolean(LOAD, pageState.load)
                    putInt(INDEX, pageIndex)
                    putInt(CHAPTER, pageState.chapterIndex)
                }
            }
    }
}