package com.example.moviepostermanagementapp.di

import android.content.Context
import com.example.moviepostermanagementapp.data.local.CineVaultDatabase
import com.example.moviepostermanagementapp.data.local.ContentItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCineVaultDatabase(@ApplicationContext context: Context): CineVaultDatabase {
        return CineVaultDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideContentItemDao(database: CineVaultDatabase): ContentItemDao {
        return database.contentItemDao()
    }
}
