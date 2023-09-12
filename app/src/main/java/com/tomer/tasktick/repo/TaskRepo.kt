package com.tomer.tasktick.repo

import com.tomer.tasktick.room.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepo {
    suspend fun getAllTasks(): Flow<List<Task>>
}