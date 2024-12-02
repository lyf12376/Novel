package com.test.novel.database.readHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert
    fun insert(searchHistory: SearchHistory)

    @Query("SELECT * FROM SearchHistory")
    fun getAll(): Flow<List<SearchHistory>>

    @Query("DELETE FROM SearchHistory")
    fun deleteAll()
}