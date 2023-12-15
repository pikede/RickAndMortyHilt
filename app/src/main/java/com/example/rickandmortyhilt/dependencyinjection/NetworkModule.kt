package com.example.rickandmortyhilt.dependencyinjection

import com.example.rickandmortyhilt.data.network.RickAndMortyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Singleton
    @Provides
    fun bindRetrofitClient(): RickAndMortyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RickAndMortyApi::class.java)
}