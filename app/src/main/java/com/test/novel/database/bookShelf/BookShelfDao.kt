package com.test.novel.database.bookShelf

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookShelfDao {
    @Insert
    suspend fun addBook(bookInShelf: BookInShelf)

    @Query("DELETE FROM BookInShelf WHERE id IN (:bookIds)")
    suspend fun deleteBooks(bookIds: List<Int>)

    @Query("SELECT * FROM BookInShelf")
    fun getAllBooks(): Flow<List<BookInShelf>>

    @Query("SELECT * FROM BookInShelf WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookInShelf?


}