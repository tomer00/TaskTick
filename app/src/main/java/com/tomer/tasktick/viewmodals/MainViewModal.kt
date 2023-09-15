package com.tomer.tasktick.viewmodals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tomer.tasktick.modals.Task
import com.tomer.tasktick.repo.TaskRepo
import com.tomer.tasktick.repo.TaskRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class MainViewModal @Inject constructor(private val repo: TaskRepo) : ViewModel() {


    //region ::TASKS-->>

    private val _tasksRv = MutableLiveData<List<Task>>()
    val tasksRv = _tasksRv

    //endregion ::TASKS-->>

    init {
        viewModelScope.launch {
           _tasksRv.value =  repo.getAllTasks()
        }
    }
}