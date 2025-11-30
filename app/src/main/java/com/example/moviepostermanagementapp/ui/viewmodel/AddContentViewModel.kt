package com.example.moviepostermanagementapp.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType
import com.example.moviepostermanagementapp.data.repository.ContentItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

data class AddContentUiState(
    val posterUri: Uri? = null,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class AddContentViewModel @Inject constructor(
    private val contentItemRepository: ContentItemRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddContentUiState())
    val uiState: StateFlow<AddContentUiState> = _uiState.asStateFlow()

    fun setPosterUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(posterUri = uri)
    }

    fun resetSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }

    fun saveContent(
        title: String,
        year: Int?,
        type: ContentType,
        status: ContentStatus,
        posterUri: Uri
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                saveError = null
            )

            try {
                val posterPath = saveImageToInternalStorage(posterUri)
                if (posterPath == null) {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveError = "Failed to save poster image."
                    )
                    return@launch
                }
                
                val contentItem = ContentItem(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    posterPath = posterPath,
                    type = type,
                    status = status,
                    releaseYear = year,
                    dateAdded = System.currentTimeMillis(),
                    dateWatched = if (status == ContentStatus.WATCHED) System.currentTimeMillis() else null
                )

                contentItemRepository.insertContentItem(contentItem)
                
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    saveSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    saveError = e.message ?: "Save failed"
                )
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = "poster_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
