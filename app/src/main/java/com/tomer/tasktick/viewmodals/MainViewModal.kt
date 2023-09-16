package com.tomer.tasktick.viewmodals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomer.tasktick.modals.Task
import com.tomer.tasktick.repo.TaskRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class MainViewModal @Inject constructor(private val repo: TaskRepo) : ViewModel() {


    private var undoCycle = true
    private lateinit var delTask: Task
    private var pos = -1
    fun removeFromRv(id: Int) {
        val nL = mutableListOf<Task>()

        _tasksRv.value!!.filterIndexed { index, task ->
            if (task.id != id) {
                nL.add(task)
                delTask = task
                pos = index
            }
            false
        }
        _tasksRv.value = nL
        _snack.value = true
        viewModelScope.launch {
            delay(3000)
            _snack.value = false
            if (!undoCycle) {
                repo.delete(delTask)
            }
        }

    }

    fun undo() {
        _snack.value = false
        undoCycle = false

    }

    //region ::TASKS-->>
    private val _tasksRv = MutableLiveData<List<Task>>()
    val tasksRv = _tasksRv

    //endregion ::TASKS-->>

    // region ::SNACK BAR-->>
    private val _snack = MutableLiveData<Boolean>()
    val snack = _snack
    //endregion ::SNACK BAR-->>

    init {
        viewModelScope.launch {
            _tasksRv.value = repo.getAllTasks(Date().time)
        }
    }
}