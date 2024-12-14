package com.test.novel.database.chapter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Insert
    suspend fun insert(chapter: List<Chapter>)

    @Query("SELECT * FROM Chapter WHERE bookId = :bookId ORDER BY chapterNumber")
    fun getChapters(bookId: Int): Flow<List<Chapter>>

    @Query("SELECT * FROM Chapter WHERE bookId = :bookId AND chapterNumber = :chapterNumber")
    suspend fun getChapter(bookId: Int, chapterNumber: Int): Chapter?

    @Query("DELETE FROM Chapter WHERE bookId = :bookId")
    suspend fun deleteBook(bookId: Int)
}