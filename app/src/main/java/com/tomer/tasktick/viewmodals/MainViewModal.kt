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


    private var undoCycle = true
    private lateinit var oldList: List<Task>
    private lateinit var delTask: Task
    private val countDown = object : CountDownTimer(3000L, 3000L) {
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            _snack.value = false
            if (undoCycle)
                repo.delete(delTask)

        }

    }

    fun removeFromRv(pos: Int) {
        val nL = mutableListOf<Task>()
        oldList = _tasksRv.value!!
        nL.addAll(oldList)
        delTask = oldList[pos]
        nL.removeAt(pos)
        _tasksRv.value = nL
        _snack.value = true
        undoCycle = true
        countDown.start()
    }

    fun undo() {
        _snack.value = false
        undoCycle = false
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
        val d = sdf.parse(sdf.format(Date()))!!
        val nd = Date(getDate().time + 86400000)
        if (d.after(nd)) {
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