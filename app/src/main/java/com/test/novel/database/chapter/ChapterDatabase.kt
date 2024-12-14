package com.test.novel.database.chapter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Chapter::class], version = 1, exportSchema = false)
abstract class ChapterDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao

    companion object {
        private const val DATABASE_NAME = "chapter_database"

        @Volatile
        private var INSTANCE: ChapterDatabase? = null

        fun getDatabase(context: Context): ChapterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ChapterDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}