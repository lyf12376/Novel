package com.test.novel.database.readHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadHistoryDao {
     @Insert
     fun insert(readHistory: ReadHistory)

     @Query("SELECT * FROM ReadHistory WHERE bookId = :bookId")
     fun getReadHistory(bookId: Int): ReadHistory

     @Query("SELECT * FROM ReadHistory")
     fun getAll(): Flow<List<ReadHistory>>

     @Query("DELETE FROM ReadHistory")
     fun deleteAll()
}