package com.test.novel.database.readHistory

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val searchContent: String
)