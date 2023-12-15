package com.example.rickandmortyhilt.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmortyhilt.data.database.converters.EpisodeListConverter
import com.example.rickandmortyhilt.data.database.converters.LocationConverter
import com.example.rickandmortyhilt.data.database.converters.OriginConverter
import com.example.rickandmortyhilt.data.database.converters.ResultConverter
import com.example.rickandmortyhilt.data.database.converters.ResultListConverter
import com.example.rickandmortyhilt.data.models.Result

@Database(exportSchema = false, version = 1, entities = [Result::class])
@TypeConverters(
    EpisodeListConverter::class,
    LocationConverter::class,
    OriginConverter::class,
    ResultConverter::class,
    ResultListConverter::class
)
abstract class CharactersDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "Characters"
    }

    abstract fun getResultDao(): ResultDao
}