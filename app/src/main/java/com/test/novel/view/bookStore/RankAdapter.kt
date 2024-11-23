package com.test.novel.view.bookStore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.test.novel.R
import com.test.novel.databinding.RankBookItemBinding
import com.test.novel.model.BookBrief
import com.test.novel.view.bookShelfPage.BookShelfViewModel

class RankAdapter(fragment: Fragment, private val viewModel: BookShelfViewModel) : RecyclerView.Adapter<RankAdapter.RankItemViewHolder>() {
    private var bookRankList : List<BookBrief> = listOf()

    inner class RankItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = RankBookItemBinding.bind(view)
        val cover = binding.bookCover
        val title = binding.bookName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rank_book_item,parent,false)
        return RankItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankItemViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return bookRankList.size
    }

    private fun updateData()
    {

    }


}