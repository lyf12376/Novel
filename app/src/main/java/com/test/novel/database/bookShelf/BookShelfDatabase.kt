package com.test.novel.database.bookShelf

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.novel.App

@Database(entities = [BookInShelf::class], version = 1, exportSchema = false)
@TypeConverters(BookInShelfConverter::class)
abstract class BookShelfDatabase:RoomDatabase() {
    abstract fun bookShelfDao():BookShelfDao

    companion object{
        @Volatile
        private var INSTANCE: BookShelfDatabase? = null

        fun getDatabase(context: Context): BookShelfDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BookShelfDatabase::class.java,
                    "book_shelf_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}