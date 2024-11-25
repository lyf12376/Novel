package com.test.novel.view.bookShelfPage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.novel.R
import com.test.novel.databinding.BookItemBinding
import com.test.novel.database.bookShelf.BookInShelf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BookAdapter(fragment: Fragment, private val viewModel: BookShelfViewModel) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var bookInShelfList: List<BookInShelf> = listOf()
    private var isDeleteMode = false


    init {
        viewModel.state.onEach { state ->
            updateData(state.changedIndex,state.bookInShelfList,state.isDeleteMode)
        }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = BookItemBinding.bind(view)
        val bookCover: ImageView = binding.bookCover
        val bookTitle: TextView = binding.bookTitle
        val checkBox:CheckBox = binding.bookCheckBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        if (isDeleteMode) {
            view.findViewById<CheckBox>(R.id.bookCheckBox).visibility = View.VISIBLE
        } else {
            view.findViewById<CheckBox>(R.id.bookCheckBox).visibility = View.INVISIBLE
        }
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookInShelfList[position]
        holder.itemView.setOnLongClickListener {
            viewModel.sendIntent(BookShelfIntent.EnterDeleteMode)
            true
        }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.sendIntent(BookShelfIntent.SelectBook(book.id))
            } else {
                viewModel.sendIntent(BookShelfIntent.UnSelectBook(book.id))
            }
        }

        holder.itemView.setOnClickListener {
            if (isDeleteMode) {
                holder.checkBox.isChecked = !holder.checkBox.isChecked
            }else{
                viewModel.sendIntent(BookShelfIntent.OpenBook(book.id))
            }
        }
        holder.bookTitle.text = book.title
        Glide.with(holder.itemView)
            .load(R.drawable.cover1)
            .into(holder.bookCover)
    }

    override fun getItemCount(): Int {
        return bookInShelfList.size
    }

    private fun updateData(changedIndex: Int, newData: List<BookInShelf>, isDeleteMode: Boolean) {
        val shouldUpdateDeleteMode = this.isDeleteMode != isDeleteMode
        val shouldUpdateData = bookInShelfList != newData

        this.isDeleteMode = isDeleteMode

        if (shouldUpdateDeleteMode) {
            notifyItemRangeChanged(0, bookInShelfList.size)
            return
        }

        if (shouldUpdateData) {
            bookInShelfList = newData
            if (changedIndex == -1) {
                notifyItemRangeChanged(0, bookInShelfList.size)
            } else {
                notifyItemChanged(changedIndex)
            }
        }
    }

}
