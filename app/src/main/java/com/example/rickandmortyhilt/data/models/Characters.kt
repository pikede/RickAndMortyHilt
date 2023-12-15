package com.example.rickandmortyhilt.data.models

data class Characters(
    val info: Info,
    val results: List<Result> = emptyList(),
)