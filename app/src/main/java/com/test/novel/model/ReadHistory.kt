package com.test.novel.model

data class ReadHistory(
    val bookBrief: BookBrief,
    val chapter: String,
    val time: Long,
    val isInBookShelf: Boolean
)
