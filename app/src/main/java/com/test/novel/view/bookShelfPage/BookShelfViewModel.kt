package com.test.novel.view.bookShelfPage

import androidx.lifecycle.ViewModel
import com.test.novel.R
import com.test.novel.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookShelfViewModel : ViewModel() {
    private val _bookShelf = MutableStateFlow(listOf<Book>())
    val bookShelf = _bookShelf.asStateFlow()

    private val _itemSelected = MutableStateFlow(0)
    val itemSelected = _itemSelected.asStateFlow()

    val bookList = listOf(
        Book("Book 1", R.drawable.cover1),
        Book("Book 2", R.drawable.cover1),
        Book("Book 3", R.drawable.cover1),
    )

    fun selectItem(position: Int) {
        _itemSelected.value = position
    }
}