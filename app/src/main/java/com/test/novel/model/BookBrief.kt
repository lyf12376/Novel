package com.test.novel.model

import kotlinx.serialization.Serializable

@Serializable
data class BookBrief(
    val title: String = "",
    val bookId: Int = 0,
    val author: String = "",
    val brief : String = ""
)
