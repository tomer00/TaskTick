package com.tomer.tasktick.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tomer.tasktick.modals.Task

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Delete
    fun deleteMany(tasks: List<Task>)

    @Query("SELECT * from tasks where created > :time ORDER by created DESC")
    fun getAllTasks(time:Long): List<Task>
}