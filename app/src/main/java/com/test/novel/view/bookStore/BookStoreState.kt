package com.test.novel.view.bookStore

import com.test.novel.model.BookBrief
import kotlinx.serialization.Serializable

@Serializable
data class BookStoreState(
    val searchBarText:String = "",
    val isRefreshing:Boolean = false,
    val isLoading:Boolean = false,
    val rank:List<BookBrief> = listOf(),
    val recommend:List<BookBrief> = listOf(),
)

sealed class BookStoreIntent{

    data object Refresh:BookStoreIntent()

    data object LoadMore:BookStoreIntent()

    data object Search:BookStoreIntent()

    data class InitData(val rank:List<BookBrief>,val recommend:List<BookBrief>):BookStoreIntent()
}


