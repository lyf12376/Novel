package com.test.novel.database.chapter

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.test.novel.database.bookShelf.BookInShelf

@Entity(
    foreignKeys = [ForeignKey(entity = BookInShelf::class, parentColumns = ["id"], childColumns = ["bookId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("bookId")]
)
data class Chapter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: Int, // 外键，关联 BookInShelf 表
    val chapterNumber: Int,
    val title: String,
    val content: String
)
