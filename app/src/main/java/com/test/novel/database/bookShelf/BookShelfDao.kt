package com.test.novel.database.bookShelf

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookShelfDao {
    @Insert
    suspend fun addBook(book: Book)

    @Query("DELETE FROM Book WHERE id = :bookId")
    suspend fun deleteBook(bookId:Int)

    @Query("SELECT * FROM Book")
    fun getAllBooks(): Flow<List<Book>>


}