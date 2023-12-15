package com.example.rickandmortyhilt.data.repository

import androidx.annotation.VisibleForTesting
import com.example.rickandmortyhilt.data.database.ResultDao
import com.example.rickandmortyhilt.data.logError
import com.example.rickandmortyhilt.data.models.Characters
import com.example.rickandmortyhilt.data.models.Locations
import com.example.rickandmortyhilt.data.models.Result
import com.example.rickandmortyhilt.data.models.ResultResponse
import com.example.rickandmortyhilt.data.network.RickAndMortyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: RickAndMortyApi, private val resultDao: ResultDao) :
    Repository {
    private val mutex = Mutex()

    override suspend fun getCharactersLocations(id: Int): Response<Locations> {
        return api.getCharactersLocations(id)
    }

    override suspend fun getCharacters(page: Int): ResultResponse<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val networkResponse = api.getCharacters(page)
                getResultResponse(networkResponse)
            } catch (e: Exception) {
                logError(e.message)
                e.getResultResponseError()
            }
        }
    }

    override suspend fun queryCharacter(
        page: Int,
        characterQueryName: String,
    ): ResultResponse<Result> = queryCharacterFromNetwork(page, characterQueryName)

    private suspend fun queryCharacterFromNetwork(
        page: Int,
        characterQueryName: String,
    ): ResultResponse<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    api.getCharactersWithQueryName(page, characterQueryName)
                getResultResponse(response)
            } catch (e: Exception) {
                logError(e.message)
                e.getResultResponseError()
            }
        }
    }

    private suspend fun getResultResponse(response: Response<Characters>): ResultResponse<Result> {
        return when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    insertResultsIntoDatabase(it.results)
                    ResultResponse.ResultList(
                        results = it.results,
                        nextPage = it.info.getNextPageInt(),
                        previousPage = it.info.getPreviousPage()
                    )
                } ?: ResultResponse.ResultList(emptyList(), nextPage = null, previousPage = null)
            }

            else -> {
                val errorBody = response.errorBody()
                ResultResponse.Error(errorBody?.toString())
            }
        }
    }

    @VisibleForTesting
    private suspend fun insertResultsIntoDatabase(response: List<Result>) {
        withContext(Dispatchers.IO) {
                resultDao.addCharacterResult(response)
        }
    }

    private fun Exception.getResultResponseError(): ResultResponse<Result> {
        return ResultResponse.Error(this.toString())
    }
}