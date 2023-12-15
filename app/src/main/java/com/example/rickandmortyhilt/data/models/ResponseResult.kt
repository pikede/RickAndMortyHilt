package com.example.rickandmortyhilt.data.models

sealed interface ResultResponse<out Data> {
    data class Error(val errorMessage: String?) : ResultResponse<Nothing>
    data class ResultList<Data>(
        val results: List<Data>,
        val nextPage: Int?,
        val previousPage: Int?,
    ) : ResultResponse<Data>
}