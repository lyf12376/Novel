package com.test.novel.database.bookShelf

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey
    val id: Int,
    val title: String,
    val coverResId: Int
)

