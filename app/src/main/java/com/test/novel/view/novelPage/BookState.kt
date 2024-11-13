package com.test.novel.view.novelPage

import com.test.novel.model.BookBrief
import kotlinx.serialization.Serializable

@Serializable
data class BookState(
    val title: String = "",
    val bookId: Int = 0,
    val author: String = "",
    val brief:String = "",
    val currentIndex: Int = 0,
    val showBar: Boolean = false,
    val pageCount: MutableList<Int> = mutableListOf(),
    val pages: MutableList<PageState> = mutableListOf()
)

@Serializable
data class PageState(
    val chapterIndex:Int = 1,
    val title:String = "",
    val text:String = "",
    val load:Boolean = false
)

sealed class BookIntent {
    data object ShowBar: BookIntent()

    data class Init(val bookBrief: BookBrief) : BookIntent()

    data class SetContent(val pages:MutableList<PageState>):BookIntent()

    data class AddPages(val pageState:List<PageState>):BookIntent()

    data class SetCurrentIndex(val index:Int):BookIntent()

}

