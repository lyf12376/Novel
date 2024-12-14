package com.test.novel.database.chapter

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.test.novel.database.bookShelf.BookInShelf
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Chapter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Int = 0,
    val chapterNumber: Int,
    val title: String,
    val content: String
)
