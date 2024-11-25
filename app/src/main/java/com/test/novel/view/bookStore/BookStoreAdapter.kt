package com.test.novel.view.bookStore

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.test.novel.R
import com.test.novel.databinding.RandomBookItemBinding
import com.test.novel.databinding.RandomViewBinding
import com.test.novel.databinding.RankBookItemBinding
import com.test.novel.databinding.RankViewBinding
import com.test.novel.model.BookBrief
import com.test.novel.utils.SizeUtils
import com.test.novel.view.customView.GridSpacingItemDecoration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BookStoreAdapter(private val fragment: Fragment, private val viewModel: BookStoreViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val rankViewAdapter = RankViewAdapter()
    private val randomViewAdapter = RandomViewAdapter()

    companion object {
        const val TYPE_RANK = 0
        const val TYPE_RANDOM = 1
    }

    inner class RankViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RankViewBinding.bind(view)
        val recommend = binding.recommendRank
        val hot = binding.hotRank
        val recommendText = binding.recommendRankText
        val hotText = binding.hotRankText
        val rankView = binding.rankBookList
    }

    inner class RandomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RandomViewBinding.bind(view)
        val randomView = binding.randomBookList
    }

    inner class RankViewAdapter : RecyclerView.Adapter<RankViewAdapter.RankViewItemHolder>() {

        init {
            viewModel.bookStoreState.onEach { state ->
                Log.d("TAG", ": $state")
                updateData(state.rank)
            }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
        }

        private var rankList: List<BookBrief> = viewModel.bookStoreState.value.rank

        inner class RankViewItemHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val binding = RankBookItemBinding.bind(view)
            val bookCover = binding.bookCover
            val bookTitle = binding.bookName
            val rank = binding.rank
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewItemHolder {
            return RankViewItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rank_book_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RankViewItemHolder, position: Int) {
            val book = rankList[position]
            holder.bookTitle.text = book.title
            holder.rank.text = book.bookId.toString()
        }

        override fun getItemCount(): Int {
            return rankList.size
        }

        private fun updateData(newList: List<BookBrief>) {
            if (newList != rankList) {
                rankList = newList
                println(this.rankList)
                notifyItemRangeChanged(0, newList.size)
            }
        }
    }

    inner class RandomViewAdapter : RecyclerView.Adapter<RandomViewAdapter.RandomViewItemHolder>() {
        init {
            viewModel.bookStoreState.onEach { state ->
                updateData(state.recommend)
            }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
        }

        private var randomList: List<BookBrief> = viewModel.bookStoreState.value.recommend

        inner class RandomViewItemHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val binding = RandomBookItemBinding.bind(view)
            val bookCover = binding.bookCover
            val bookTitle = binding.bookName
            val type = binding.bookType
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomViewItemHolder {
            return RandomViewItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.random_book_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RandomViewItemHolder, position: Int) {
            val book = randomList[position]
            holder.bookTitle.text = book.title
            holder.type.text = book.author
        }

        override fun getItemCount(): Int {
            return randomList.size
        }

        private fun updateData(newList: List<BookBrief>) {
            Log.e("TAG", "updateData: $newList")
            if (newList != randomList) {
                randomList = newList
                notifyItemRangeChanged(0, newList.size)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_RANK
            1 -> TYPE_RANDOM
            else -> TYPE_RANK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        println("dsadasdasdasdasdd")
        return when (viewType) {
            TYPE_RANK -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.rank_view, parent, false)
                RankViewHolder(view)
            }

            TYPE_RANDOM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.random_view, parent, false)
                RandomViewHolder(view)
            }

            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.rank_view, parent, false)
                RankViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RankViewHolder -> {
                holder.rankView.adapter = rankViewAdapter
                // 其他逻辑
            }
            is RandomViewHolder -> {
                holder.randomView.adapter = randomViewAdapter
                val spacing = SizeUtils.dp2px(8f) // 间距大小，单位为 px
                val includeEdge = true // 是否包括边缘间距
                holder.randomView.addItemDecoration(
                    GridSpacingItemDecoration(2, spacing, spacing, includeEdge)
                )
            }
        }
    }


    override fun getItemCount(): Int {
        return 2
    }


}