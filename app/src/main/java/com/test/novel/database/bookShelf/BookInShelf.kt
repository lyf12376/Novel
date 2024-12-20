package com.test.novel.database.bookShelf

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.novel.model.BookBrief
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class BookInShelf(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int = 0,
    val isLocal: Boolean = false,
    val title: String = "",
    val coverUrl: String = "",
    val author: String = "author",
    val type:List<String> = listOf("玄幻","恋爱"),
    val readChapters:Int = 0,//已读章节
    val totalChapters:Int = 0
){
    fun getBrief():BookBrief{
        return BookBrief(
            title = title,
            bookId = bookId,
            author = author,
            type = type,
            isLocal = isLocal,
            coverUrl = coverUrl
        )
    }
}

