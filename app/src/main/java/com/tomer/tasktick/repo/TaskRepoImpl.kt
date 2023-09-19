package com.tomer.tasktick.repo

import android.app.Application
import android.content.Context
import com.tomer.tasktick.modals.Task
import com.tomer.tasktick.modals.TaskRoom
import com.tomer.tasktick.room.Dao
import java.util.Calendar
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(private val dao: Dao, private val con: Application) : TaskRepo {

    private val pref by lazy { con.getSharedPreferences("tasks", Context.MODE_PRIVATE) }
    private val last: Int = pref.getInt("last", 0)

    override suspend fun getAllTasks(time: Long): List<Task> {
        val d = dao.getAllTasks(last).filter {
            time > it.timeCreated.toLong()
        }
        val a = mutableListOf<Task>()
        d.forEach {
            a.add(change(it))
        }
        return a
    }

    override fun delete(delTask: Task) {
        dao.deleteTask(delTask.id)
    }

    override fun saveNewTask(newTask: Task) {
        dao.insertTask(change(newTask))
    }

    override fun updateTask(id: Int, time: Long) {
        dao.updateTask(time.toString(), id)
    }

    override suspend fun getNext(time: Long): Task? {
        val c = Calendar.getInstance()
        c.timeInMillis = time
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        val d = dao.getAllTasks(last).filter { i ->
            time < i.timeCreated.toLong() && i.timeCreated.toLong() < c.timeInMillis
        }
        return if (d.isEmpty()) null
        else change(d[0])
    }


    override fun getSingleTask(id: Int) {

    }

    private fun change(t: TaskRoom): Task {
        return Task(t.id, t.des, t.timeCreated.toLong(), t.timeDone.toLong(), t.priority, t.isDone)
    }

    private fun change(t: Task): TaskRoom {
        return TaskRoom(t.id, t.des, t.timeCreated.toString(), t.timeDone.toString(), t.priority, t.isDone)
    }

    override fun setLast() {
        pref.edit().putInt("last", dao.getMaxId()).apply()
    }
}