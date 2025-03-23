package com.test.novel.view.searchPage

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.test.novel.database.searchHistory.SearchHistory
import com.test.novel.databinding.SearchHistoryItemBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchHistoryAdapter(
    private val fragment: Fragment,
    private val viewModel: SearchViewModel,
    private val gotoSearch:()->Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    private var searchHistoryList = listOf<SearchHistory>()

    init {
        viewModel.searchState.onEach{
            updateData(it.searchHistory)
        }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    inner class SearchHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = SearchHistoryItemBinding.bind(view)
        val searchHistory = binding.searchHistory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val view = SearchHistoryItemBinding.inflate(fragment.layoutInflater, parent, false).root
        return SearchHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.searchHistory.text = searchHistoryList[position].searchContent
        holder.itemView.setOnClickListener {
            viewModel.sendIntent(SearchIntent.Search(searchHistoryList[position].searchContent))
            gotoSearch()
        }
        holder
    }

    private fun updateData(newList:List<SearchHistory>){
        if (newList != searchHistoryList){
            searchHistoryList = newList
            notifyDataSetChanged()
        }
    }
}