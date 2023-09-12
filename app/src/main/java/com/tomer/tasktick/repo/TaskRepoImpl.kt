package com.tomer.tasktick.repo

import com.tomer.tasktick.room.Dao
import com.tomer.tasktick.room.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(private val dao: Dao) : TaskRepo {
    override suspend fun getAllTasks(): Flow<List<Task>> {
        return flowOf(dao.getAllTasks())
    }


}