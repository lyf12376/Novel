package com.test.novel.view.novelPage

import com.test.novel.model.BookBrief
import kotlinx.serialization.Serializable

@Serializable
data class BookState(
    val title: String = "",
    val bookId: Int = 0,
    val author: String = "",
    val brief:String = "",
    val content: List<String> = emptyList(),
    val currentIndex: Int = 0,
    val showBar: Boolean = true,
    val everyPage: MutableList<MutableList<String>> = mutableListOf()
)

sealed class BookIntent() {
    data object ShowBar: BookIntent()

    data class Init(val bookBrief: BookBrief) : BookIntent()

    data class SetContent(val text:MutableList<MutableList<String>>):BookIntent()

    data class AddPage(val rowIndex:Int,val columnIndex:Int):BookIntent()
}

