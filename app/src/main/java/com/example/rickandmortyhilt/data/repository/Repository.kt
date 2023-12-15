package com.example.rickandmortyhilt.data.repository

import com.example.rickandmortyhilt.data.models.Locations
import com.example.rickandmortyhilt.data.models.Result
import com.example.rickandmortyhilt.data.models.ResultResponse
import retrofit2.Response
import retrofit2.http.Path

interface Repository {
    suspend fun getCharactersLocations(id: Int): Response<Locations>

    suspend fun getCharacters(page: Int): ResultResponse<Result>

    suspend fun queryCharacter(page: Int, characterQueryName: String): ResultResponse<Result>
}