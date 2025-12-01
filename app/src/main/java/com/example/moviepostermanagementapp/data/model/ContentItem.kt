package com.example.moviepostermanagementapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_items")
data class ContentItem(
    @PrimaryKey
    val id: String,
    val title: String,
    val posterPath: String,
    val type: ContentType,
    val status: ContentStatus,
    val imdbRating: Float? = null,
    val rottenTomatoesScore: Int? = null,
    val description: String? = null,
    val releaseYear: Int? = null,
    val director: String? = null,
    val cast: List<String> = emptyList(),
    val genre: List<String> = emptyList(),
    val runtime: Int? = null,
    val userRating: Float? = null,
    val userNotes: String? = null,
    val publicRatings: List<PublicRating> = emptyList(),
    val dateAdded: Long,
    val dateWatched: Long? = null,
    val imdbId: String? = null
)
