package com.example.moviepostermanagementapp.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromContentType(value: ContentType): String {
        return value.name
    }

    @TypeConverter
    fun toContentType(value: String): ContentType {
        return ContentType.valueOf(value)
    }

    @TypeConverter
    fun fromContentStatus(value: ContentStatus): String {
        return value.name
    }

    @TypeConverter
    fun toContentStatus(value: String): ContentStatus {
        return ContentStatus.valueOf(value)
    }

    @TypeConverter
    fun fromPublicRatingList(value: List<PublicRating>): String {
        val gson = Gson()
        val type = object : TypeToken<List<PublicRating>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPublicRatingList(value: String): List<PublicRating> {
        if (value.isEmpty()) {
            return emptyList()
        }
        val gson = Gson()
        val type = object : TypeToken<List<PublicRating>>() {}.type
        return gson.fromJson(value, type)
    }
}
