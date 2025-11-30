package com.example.moviepostermanagementapp.data.repository

import com.example.moviepostermanagementapp.data.local.ContentItemDao
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentItemRepository @Inject constructor(
    private val contentItemDao: ContentItemDao
) {
    fun getAllContentItems(): Flow<List<ContentItem>> = contentItemDao.getAllContentItems()

    fun getContentItemsByStatus(status: ContentStatus): Flow<List<ContentItem>> = 
        contentItemDao.getContentItemsByStatus(status)

    fun searchContentItems(query: String): Flow<List<ContentItem>> = 
        contentItemDao.searchContentItems(query)

    suspend fun getContentItemById(id: String): ContentItem? = 
        contentItemDao.getContentItemById(id)

    suspend fun insertContentItem(contentItem: ContentItem) = 
        contentItemDao.insertContentItem(contentItem)

    suspend fun updateContentItem(contentItem: ContentItem) = 
        contentItemDao.updateContentItem(contentItem)

    suspend fun deleteContentItem(contentItem: ContentItem) = 
        contentItemDao.deleteContentItem(contentItem)

    suspend fun deleteContentItemById(id: String) = 
        contentItemDao.deleteContentItemById(id)

    suspend fun getContentItemCountByStatus(status: ContentStatus): Int = 
        contentItemDao.getContentItemCountByStatus(status)

    fun getContentItemsByType(type: ContentType): Flow<List<ContentItem>> = 
        contentItemDao.getContentItemsByType(type)

    fun getContentItemsByGenre(genre: String): Flow<List<ContentItem>> = 
        contentItemDao.getContentItemsByGenre(genre)
}
