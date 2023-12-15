package com.example.rickandmortyhilt.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rickandmortyhilt.data.models.Result

@Dao
interface ResultDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacterResult(characterResult: Result)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacterResult(characterResults: List<Result>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacterResult(vararg characterResults: Result)

    @Query("SELECT * FROM characters WHERE name LIKE :name")
    suspend fun getCharacters(name: String): List<Result>

    @Query("SELECT * FROM characters ORDER BY name")
    suspend fun getCharacters(): List<Result>
}