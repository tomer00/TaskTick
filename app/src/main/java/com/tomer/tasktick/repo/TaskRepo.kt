package com.tomer.tasktick.repo

import com.tomer.tasktick.modals.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepo {
    suspend fun getAllTasks(time:Long): List<Task>
    fun delete(delTask: Task)
    fun saveNewTask(newTask: Task)
    fun updateTask(id:Int,time: Long)
    suspend fun getNext(time:Long):Task?
    fun getSingleTask(id:Int)
    fun setLast()
}