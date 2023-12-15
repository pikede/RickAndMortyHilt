package com.example.rickandmortyhilt.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.example.rickandmortyhilt.data.database.CharactersDatabase
import com.example.rickandmortyhilt.data.database.ResultDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): CharactersDatabase {
        return Room.databaseBuilder(
            context,
            CharactersDatabase::class.java,
            CharactersDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun bindResultDao(charactersDatabase: CharactersDatabase): ResultDao {
        return charactersDatabase.getResultDao()
    }

}