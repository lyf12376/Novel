package com.test.novel.view.novelPage

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PageFragmentAdapter(fragment: Fragment, viewModel: NovelFragmentViewModel) :
    FragmentStateAdapter(fragment) {

    private var data: MutableList<MutableList<String>> = mutableListOf()

    init {
        viewModel.state.onEach { state ->
            Log.d("TAG", ": ${state.everyPage}")
            updateData(state.everyPage)
        }.launchIn(fragment.viewLifecycleOwner.lifecycleScope) // 使用 viewLifecycleOwner 的作用域
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(data[position].joinToString("\n"))
    }

    private fun updateData(newData: MutableList<MutableList<String>>) {
        if (data == newData) return
        data.add(newData.last())
        notifyItemInserted(data.size - 1)
    }
}


