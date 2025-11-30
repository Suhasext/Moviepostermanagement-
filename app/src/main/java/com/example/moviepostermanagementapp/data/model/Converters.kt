package com.example.moviepostermanagementapp.data.model

import androidx.room.TypeConverter

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
}
