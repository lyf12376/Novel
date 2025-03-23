package com.test.novel.database.readHistory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReadHistory::class], version = 1, exportSchema = false)
abstract class ReadHistoryDatabase: RoomDatabase() {
    abstract fun readHistoryDao(): ReadHistoryDao

    companion object {
        private const val DATABASE_NAME = "read_history_database"

        @Volatile
        private var INSTANCE: ReadHistoryDatabase? = null

        fun getDatabase(context: Context): ReadHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ReadHistoryDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}