package com.tomer.tasktick.viewmodals

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomer.tasktick.modals.Task
import com.tomer.tasktick.repo.TaskRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModal @Inject constructor(private val repo: TaskRepo, con: Application) : ViewModel() {

    //region DAY

    private val pref = con.getSharedPreferences("tasks", Context.MODE_PRIVATE)

    private fun getDate(): Date {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return sdf.parse(pref.getString("date", "15/01/2021").toString())!!
    }

    private val upperTime: Long
    //endregion DAY

    private lateinit var oldList: List<Task>
    private var delTask: Task = Task(-1, "", 0L, 0L, 0, false)
    private val countDown = object : CountDownTimer(3000L, 3000L) {
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            _snack.value = false
            repo.delete(delTask)
        }

    }

    fun removeFromRv(id: Int) {
        if (_snack.value == true) {
            countDown.cancel()
            repo.delete(delTask)
            countDown.start()
        } else {
            _snack.value = true
            countDown.start()
        }
        val nL = mutableListOf<Task>()
        oldList = _tasksRv.value!!
        nL.addAll(oldList)
        var ir = 0
        for (i in oldList.indices) {
            if (oldList[i].id == id) {
                delTask = oldList[i]
                ir = i
                break
            }
        }
        nL.removeAt(ir)
        _tasksRv.value = nL
    }

    fun undo() {
        _snack.value = false
        _tasksRv.value = oldList
        countDown.cancel()
    }

    fun saveTask(task: Task) {
        repo.saveNewTask(task)
        updateRv()
    }

    private fun updateRv() {
        viewModelScope.launch {
            _tasksRv.value = repo.getAllTasks(upperTime)
            val t = repo.getNext(Date().time)
            if (t != null) _currentTask.value = t!!
        }
    }

    fun updateTask(pos: Int) {
        val t = _tasksRv.value?.get(pos)
        if (t != null) {
            repo.updateTask(t.id, Date().time)
            _tasksRv.value?.get(pos)!!.isDone = true
        }
    }

    //region ::TASKS-->>
    private val _tasksRv = MutableLiveData<List<Task>>()
    val tasksRv = _tasksRv

    private val _currentTask = MutableLiveData<Task>()
    val currentTask = _currentTask
    //endregion ::TASKS-->>

    // region ::SNACK BAR-->>
    private val _snack = MutableLiveData<Boolean>()
    val snack = _snack
    //endregion ::SNACK BAR-->>

    init {

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val nd = Date(getDate().time + 86400000)
        if (Date().after(nd)) {
            repo.setLast()
            pref.edit().putString("date", sdf.format(Date())).apply()
        }

        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)

        upperTime = c.timeInMillis

        updateRv()
    }
}