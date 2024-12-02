package com.test.novel.view.searchPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.novel.R
import com.test.novel.databinding.SearchResultItemBinding
import com.test.novel.network.search.SearchResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchResultAdapter(
    private val fragment: Fragment,
    viewModel: SearchViewModel,
    private val back: () -> Unit
) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private var searchResult = listOf<SearchResult>()

    init {
        viewModel.searchState.onEach{
            updateData(it.searchResult)
        }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    private fun updateData(result: List<SearchResult>) {
        if (result != searchResult) {
            val originLength = searchResult.size
            searchResult = result
            notifyItemRangeInserted(originLength, searchResult.size)
            notifyItemRangeChanged(originLength, searchResult.size)
        }
    }

    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = SearchResultItemBinding.bind(view)
        val cover: ImageView = binding.cover
        val title = binding.bookName
        val author = binding.bookAuthor
        val status = binding.status
        val category = binding.category
        val characterCount = binding.characterCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)

        return SearchResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        println(searchResult.size)
        return searchResult.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResult = searchResult[position]
        holder.title.text = searchResult.articlename
        holder.author.text = searchResult.author
        Glide.with(fragment)
            .load(searchResult.url_img)
            .into(holder.cover)
    }
}