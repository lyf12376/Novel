package com.test.novel.view.novelPage

import android.graphics.pdf.PdfDocument.Page
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PageFragmentAdapter(fragment: Fragment, viewModel: NovelFragmentViewModel) :
    FragmentStateAdapter(fragment) {

    private var pages: MutableList<PageState> = mutableListOf()
    private var load:MutableList<Boolean> = mutableListOf()

    init {
        viewModel.state.onEach { state ->
            Log.d("TAG", ": ${state.pages}")
            updateData(state.pages)
        }.launchIn(fragment.viewLifecycleOwner.lifecycleScope) // 使用 viewLifecycleOwner 的作用域
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(pages[position])
    }

    private fun updateData(newPage:MutableList<PageState>){
        if (pages == newPage) return
        pages = newPage
        notifyItemInserted(pages.size - 1)
    }
}


