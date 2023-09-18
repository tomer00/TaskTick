package com.tomer.tasktick.viewmodals

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tomer.tasktick.R
import com.tomer.tasktick.modals.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TaskViewModal @Inject constructor(private val gson: Gson) : ViewModel() {
    fun setTaskDes(data: String) {
        textEt = data
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, _day.value ?: 0)
        c.set(Calendar.HOUR_OF_DAY, timeList[_time.value ?: 4])
        val task = Task(
            des = URLEncoder.encode(data, "UTF-8"),
            priority = _prior.value!!, timeCreated = c.time.time
        )
        _finalTask.value = gson.toJson(task)
    }

    fun setUI(str: String) {
        val task = gson.fromJson(str, Task::class.java)
        _isNew.value = false
        _prior.value = task.priority
        val c = Calendar.getInstance()

        //days
        val dif = c.time.time - Date(task.timeCreated).time
        _day.value = (dif / 86400000).toInt()

        //time
        c.time = Date(task.timeCreated)
        val h = c.get(Calendar.HOUR_OF_DAY)
        var ind = 0
        timeList.filterIndexed { index, i ->
            if (i == h) ind = index
            true
        }
        _time.value = ind
        textEt = task.des
    }

    val cli = View.OnClickListener {
        when (it.id) {
            R.id.btPriorMed -> _prior.value = 2
            R.id.btPriorLow -> _prior.value = 1
            R.id.btPriorHigh -> _prior.value = 3
        }
    }
    val daysCli = View.OnClickListener { v ->
        val t = v.tag as Int
        _day.value = t
    }

    val timeCli = View.OnClickListener { v ->
        val t = v.tag as Int
        _time.value = t
    }

    private val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val listTime = mutableListOf(
        "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM",
        "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM",
        "9 PM", "10 PM", "11 PM", "12 AM", "1 AM", "2 AM", "3 AM", "4 AM"
    )
    val timeList = mutableListOf(
        5, 6, 7, 8, 9, 10, 11, 12,
        13, 14, 15, 16, 17, 18, 19,
        20, 21, 22, 23, 0, 1, 2, 3, 4
    )

    //region DATES
    private val _dates = MutableLiveData<List<String>>()
    val dates = _dates
    //endregion DATES

    // region DAY
    private val _day = MutableLiveData<Int>()
    val day = _day
    //endregion DAY

    // region TIME
    private val _time = MutableLiveData<Int>()
    val time = _time
    //endregion TIME

    // region Prior
    private val _prior = MutableLiveData(1)
    val prior = _prior
    //endregion Prior

    // region isNew
    private val _isNew = MutableLiveData(true)
    val isNew = _isNew
    //endregion isNew

    // region textEt
    var textEt = ""
    //endregion textEt

    // region finalTask
    private val _finalTask = MutableLiveData<String>()
    val finalTask = _finalTask
    //endregion finalTask


    init {
        val date = Calendar.getInstance()
        val stirs = mutableListOf<String>()
        date.add(Calendar.DAY_OF_YEAR, 1)
        for (i in 2..5) {
            date.add(Calendar.DAY_OF_YEAR, 1)
            stirs.add("${date.get(Calendar.DAY_OF_MONTH)} ${months[date.get(Calendar.MONTH)]}")
        }
        _dates.value = stirs
    }
}