package com.example.moviepostermanagementapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.Converters

@Database(
    entities = [ContentItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CineVaultDatabase : RoomDatabase() {
    abstract fun contentItemDao(): ContentItemDao

    companion object {
        @Volatile
        private var INSTANCE: CineVaultDatabase? = null

        fun getDatabase(context: Context): CineVaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CineVaultDatabase::class.java,
                    "cinevault_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
