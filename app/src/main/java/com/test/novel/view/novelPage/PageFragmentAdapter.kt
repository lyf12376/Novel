package com.test.novel.view.novelPage

import android.graphics.pdf.PdfDocument.Page
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.novel.model.BookBrief
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PageFragmentAdapter(fragment: Fragment, private val viewModel: NovelFragmentViewModel) :
    FragmentStateAdapter(fragment) {

    private var pages: MutableList<PageState> = mutableListOf()

    init {
        viewModel.state.onEach { state ->
            updateData(state.pages, state.currentIndex)
        }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    override fun getItemCount(): Int {
        return pages.size + 1
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            BookBriefFragment.newInstance(bookBrief = viewModel.state.value.getBrief())
        } else {
            if (position - 1 < pages.size) {
                PageFragment.newInstance(pages[position - 1], position - 1)
            } else {
                // Fallback in case of index issues
                PageFragment.newInstance(
                    PageState(chapterIndex = 0, title = "", text = "", load = false),
                    position - 1
                )
            }
        }
    }

    private fun updateData(newPages: MutableList<PageState>, currentIndex: Int) {
        val oldSize = pages.size

        // Check if there are actual changes to avoid unnecessary updates
        if (pages.size == newPages.size && pages.containsAll(newPages)) return

        // Update the pages list
        pages = newPages.toMutableList()

        // Notify adapter about changes
        if (oldSize < pages.size) {
            // New pages were added
            notifyItemRangeInserted(oldSize + 1, pages.size - oldSize)
        } else if (oldSize > pages.size) {
            // Pages were removed
            notifyItemRangeRemoved(pages.size + 1, oldSize - pages.size)
        }

        // Notify that existing pages might have changed
        notifyItemRangeChanged(1, pages.size.coerceAtMost(oldSize))
    }
}


