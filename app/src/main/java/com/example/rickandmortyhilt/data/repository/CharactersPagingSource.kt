package com.example.rickandmortyhilt.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyhilt.data.models.Result
import com.example.rickandmortyhilt.data.models.ResultResponse
import retrofit2.HttpException
import java.io.IOException

class CharactersPagingSource(private val repository: Repository, private val query: String) :
    PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val pageNumber = params.key ?: 1
            val response = getResponse(pageNumber)
            getResult(response)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getResponse(pageNumber: Int): ResultResponse<Result> {
        return when (query.isEmpty()) {
            true -> repository.getCharacters(pageNumber)
            else -> repository.queryCharacter(pageNumber, query)
        }
    }

    private fun getResult(response: ResultResponse<Result>): LoadResult<Int, Result> {
        return when (response) {
            is ResultResponse.ResultList -> {
                LoadResult.Page(
                    data = response.results,
                    nextKey = response.nextPage,
                    prevKey = null // Only paging forward.
                )
            }

            is ResultResponse.Error -> {
                LoadResult.Error(Exception(response.errorMessage))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}