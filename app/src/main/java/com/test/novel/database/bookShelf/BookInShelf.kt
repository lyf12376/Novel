package com.test.novel.database.bookShelf

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class BookInShelf(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String = "",
    val coverUrl: String = "",
    val readChapters:Int = 0,//已读章节
    val totalChapters:Int = 0
)

