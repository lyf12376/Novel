package com.test.novel.database.bookShelf

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class BookInShelfConverter {
    @TypeConverter
    fun fromJson(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toJson(value: List<String>): String {
        return Json.encodeToString(value)
    }
}