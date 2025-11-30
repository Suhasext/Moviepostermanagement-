package com.example.moviepostermanagementapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.PublicRating
import com.example.moviepostermanagementapp.data.repository.ContentItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContentDetailUiState(
    val contentItem: ContentItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ContentDetailViewModel @Inject constructor(
    private val contentItemRepository: ContentItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContentDetailUiState())
    val uiState: StateFlow<ContentDetailUiState> = _uiState.asStateFlow()

    fun loadContentItem(contentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val contentItem = contentItemRepository.getContentItemById(contentId)
                _uiState.value = _uiState.value.copy(
                    contentItem = contentItem,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun toggleStatus() {
        val currentItem = _uiState.value.contentItem ?: return
        
        viewModelScope.launch {
            try {
                val newStatus = if (currentItem.status == ContentStatus.WATCHED) {
                    ContentStatus.WATCHLIST
                } else {
                    ContentStatus.WATCHED
                }
                
                val updatedItem = currentItem.copy(
                    status = newStatus,
                    dateWatched = if (newStatus == ContentStatus.WATCHED) System.currentTimeMillis() else null
                )
                
                contentItemRepository.updateContentItem(updatedItem)
                _uiState.value = _uiState.value.copy(contentItem = updatedItem)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteContent() {
        val currentItem = _uiState.value.contentItem ?: return
        
        viewModelScope.launch {
            try {
                contentItemRepository.deleteContentItem(currentItem)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateUserRatingAndNotes(rating: Float, notes: String, publicRatings: List<PublicRating>) {
        val currentItem = _uiState.value.contentItem ?: return
        
        viewModelScope.launch {
            try {
                val updatedItem = currentItem.copy(
                    userRating = if (rating > 0f) rating else null,
                    userNotes = notes.ifBlank { null },
                    publicRatings = publicRatings
                )
                contentItemRepository.updateContentItem(updatedItem)
                _uiState.value = _uiState.value.copy(contentItem = updatedItem)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
