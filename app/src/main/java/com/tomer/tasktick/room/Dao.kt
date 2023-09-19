package com.tomer.tasktick.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tomer.tasktick.modals.TaskRoom

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskRoom)

    @Query("UPDATE tasks SET comp = :c , isDone = 1 WHERE id = :idL")
    fun updateTask(c: String,idL: Int)

    @Query("Delete from tasks where id = :taskID")
    fun deleteTask(taskID: Int)

    @Query("SELECT * from tasks where id > :idL ORDER BY created")
    fun getAllTasks(idL:Int): List<TaskRoom>

    @Query("SELECT max(id) from tasks")
    fun getMaxId():Int

}