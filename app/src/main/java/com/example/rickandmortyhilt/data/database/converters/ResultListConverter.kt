package com.example.rickandmortyhilt.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.rickandmortyhilt.data.models.Result

class ResultListConverter {
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Result>>() {}.type

    @TypeConverter
    fun convertResultsToJsonString(result: List<Result>?): String? {
        return result?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun convertJsonToResults(result: String?): List<Result>? {
        return result?.let {
            gson.fromJson(it, typeToken)
        }
    }
}