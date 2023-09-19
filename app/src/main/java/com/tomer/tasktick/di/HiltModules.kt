package com.tomer.tasktick.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.tomer.tasktick.repo.TaskRepo
import com.tomer.tasktick.repo.TaskRepoImpl
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
    fun provideTaskDao(appDatabase: Database): Dao = appDatabase.taskDao()


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(
            appContext,
            Database::class.java,
            "TaskDB"
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideTaskRepo(taskRepoImpl: TaskRepoImpl): TaskRepo = taskRepoImpl

}