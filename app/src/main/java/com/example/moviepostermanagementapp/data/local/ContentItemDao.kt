package com.example.moviepostermanagementapp.data.local

import androidx.room.*
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentItemDao {
    @Query("SELECT * FROM content_items ORDER BY dateAdded DESC")
    fun getAllContentItems(): Flow<List<ContentItem>>

    @Query("SELECT * FROM content_items WHERE status = :status ORDER BY dateAdded DESC")
    fun getContentItemsByStatus(status: ContentStatus): Flow<List<ContentItem>>

    @Query("SELECT * FROM content_items WHERE title LIKE '%' || :query || '%' ORDER BY dateAdded DESC")
    fun searchContentItems(query: String): Flow<List<ContentItem>>

    @Query("SELECT * FROM content_items WHERE id = :id")
    suspend fun getContentItemById(id: String): ContentItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentItem(contentItem: ContentItem)

    @Update
    suspend fun updateContentItem(contentItem: ContentItem)

    @Delete
    suspend fun deleteContentItem(contentItem: ContentItem)

    @Query("DELETE FROM content_items WHERE id = :id")
    suspend fun deleteContentItemById(id: String)

    @Query("SELECT COUNT(*) FROM content_items WHERE status = :status")
    suspend fun getContentItemCountByStatus(status: ContentStatus): Int

    @Query("SELECT * FROM content_items WHERE type = :type ORDER BY dateAdded DESC")
    fun getContentItemsByType(type: ContentType): Flow<List<ContentItem>>

    @Query("SELECT * FROM content_items WHERE genre LIKE '%' || :genre || '%' ORDER BY dateAdded DESC")
    fun getContentItemsByGenre(genre: String): Flow<List<ContentItem>>
}
