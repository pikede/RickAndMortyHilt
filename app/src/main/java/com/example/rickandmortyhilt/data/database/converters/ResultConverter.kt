package com.example.rickandmortyhilt.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.example.rickandmortyhilt.data.models.Result

class ResultConverter {
    private val gson = Gson()

    @TypeConverter
    fun convertResultToJsonString(result: Result?): String? {
        return gson.toJson(result)
    }

    @TypeConverter
    fun convertJsonToResult(result: String): Result? {
        return gson.fromJson(result, Result::class.java)
    }
}