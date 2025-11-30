package com.example.moviepostermanagementapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType
import com.example.moviepostermanagementapp.data.repository.ContentItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val allContentItems: List<ContentItem> = emptyList(),
    val watchedContentItems: List<ContentItem> = emptyList(),
    val watchlistContentItems: List<ContentItem> = emptyList(),
    val movies: List<ContentItem> = emptyList(),
    val shows: List<ContentItem> = emptyList(),
    val watchedMovies: List<ContentItem> = emptyList(),
    val watchedShows: List<ContentItem> = emptyList(),
    val watchlistMovies: List<ContentItem> = emptyList(),
    val watchlistShows: List<ContentItem> = emptyList(),
    val searchResults: List<ContentItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val shuffledItems: List<ContentItem> = emptyList(),
    val completedMovies: Int = 0,
    val completedShows: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val contentItemRepository: ContentItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadContentItems()
    }

    private fun loadContentItems() {
        viewModelScope.launch {
            contentItemRepository.getAllContentItems().catch { throwable ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = throwable.message
                )
            }.collect { allItems ->
                val watchedItems = allItems.filter { it.status == ContentStatus.WATCHED }
                val watchlistItems = allItems.filter { it.status == ContentStatus.WATCHLIST }
                val movies = allItems.filter { it.type == ContentType.MOVIE }
                val shows = allItems.filter { it.type == ContentType.SERIES }

                _uiState.value = MainUiState(
                    allContentItems = allItems,
                    watchedContentItems = watchedItems,
                    watchlistContentItems = watchlistItems,
                    movies = movies,
                    shows = shows,
                    watchedMovies = movies.filter { it.status == ContentStatus.WATCHED },
                    watchedShows = shows.filter { it.status == ContentStatus.WATCHED },
                    watchlistMovies = movies.filter { it.status == ContentStatus.WATCHLIST },
                    watchlistShows = shows.filter { it.status == ContentStatus.WATCHLIST },
                    isLoading = false,
                    shuffledItems = allItems.shuffled(),
                    completedMovies = movies.count { it.status == ContentStatus.WATCHED },
                    completedShows = shows.count { it.status == ContentStatus.WATCHED }
                )
            }
        }
    }

    fun shuffleItems() {
        _uiState.value = _uiState.value.copy(shuffledItems = _uiState.value.allContentItems.shuffled())
    }


    fun toggleContentStatus(contentItem: ContentItem) {
        viewModelScope.launch {
            try {
                val newStatus = if (contentItem.status == ContentStatus.WATCHED) {
                    ContentStatus.WATCHLIST
                } else {
                    ContentStatus.WATCHED
                }

                val updatedItem = contentItem.copy(
                    status = newStatus,
                    dateWatched = if (newStatus == ContentStatus.WATCHED) System.currentTimeMillis() else null
                )

                contentItemRepository.updateContentItem(updatedItem)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteContentItem(contentItem: ContentItem) {
        viewModelScope.launch {
            try {
                contentItemRepository.deleteContentItem(contentItem)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun searchContent(query: String) {
        if (query.isBlank()) {
            clearSearch()
            return
        }

        viewModelScope.launch {
            try {
                contentItemRepository.searchContentItems(query).collect { results ->
                    _uiState.value = _uiState.value.copy(searchResults = results)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchResults = emptyList())
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
