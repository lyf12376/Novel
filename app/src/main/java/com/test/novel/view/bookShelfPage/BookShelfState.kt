package com.test.novel.view.bookShelfPage

import com.test.novel.database.bookShelf.BookInShelf
import kotlinx.serialization.Serializable


@Serializable
data class BookShelfState(

    val bookInShelfList:List<BookInShelf> = listOf(),

    val changedIndex:Int = -1,

    val isDeleteMode:Boolean = false,

    val deleteBooks : List<Int> = listOf(),

    val openId : Int = -1
)

sealed class BookShelfIntent {

    data class SelectBook(val id: Int) : BookShelfIntent()

    data class UnSelectBook(val id: Int) : BookShelfIntent()

    data class OpenBook(val id: Int) : BookShelfIntent()

    data object EnterDeleteMode:BookShelfIntent()

    data object DeleteBooks:BookShelfIntent()

    data object Cancel:BookShelfIntent()
}