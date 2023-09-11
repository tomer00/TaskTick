package com.tomer.tasktick.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.tomer.tasktick.room.Dao
import com.tomer.tasktick.room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HiltModules {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()


    @Provides
    @Singleton
    fun provideChannelDao(appDatabase: Database): Dao = appDatabase.channelDao()


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(
            appContext,
            Database::class.java,
            "DB"
        ).allowMainThreadQueries().build()
    }

}