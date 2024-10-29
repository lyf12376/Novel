package com.test.novel.view.bookShelfPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.novel.R
import com.test.novel.databinding.BookItemBinding
import com.test.novel.model.Book

class BookAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = BookItemBinding.bind(view)
        val bookCover: ImageView = binding.bookCover
        val bookTitle: TextView = binding.bookTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookTitle.text = book.title
        holder.bookCover.setImageResource(book.coverResId) // 使用图片资源
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}
