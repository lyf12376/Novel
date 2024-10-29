package com.test.novel.view.novelPage

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageFragmentAdapter(fragment: Fragment, private val data:List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(data[position])
    }
}