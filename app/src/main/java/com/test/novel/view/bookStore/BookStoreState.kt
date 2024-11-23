package com.test.novel.view.bookStore

import com.test.novel.model.BookBrief

data class BookStoreState(
    val searchBarText:String,
    val rank:List<BookBrief>,
)
