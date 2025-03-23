package com.test.novel.database.readHistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadHistory(
    @PrimaryKey
    val bookId: Int = 0,
    val chapterNumber: Int = 1,
    val chapterTitle: String = "",
)