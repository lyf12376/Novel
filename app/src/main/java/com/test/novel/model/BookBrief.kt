package com.test.novel.model

import kotlinx.serialization.Serializable

@Serializable
data class BookBrief(
    val title: String = "title",
    val bookId: Int = 0,
    val coverUrl: String = "coverUrl",
    val author: String = "author",
    val type:List<String> = listOf("玄幻","恋爱"),
    val status:String = "status",
    val brief : String = "brief",
)
