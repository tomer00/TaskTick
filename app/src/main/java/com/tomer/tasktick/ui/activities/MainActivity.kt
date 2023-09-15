package com.tomer.tasktick.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.tomer.tasktick.adap.TaskAdap
import com.tomer.tasktick.databinding.ActivityMainBinding
import com.tomer.tasktick.viewmodals.MainViewModal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() , TaskAdap.CallbackClick {

    private val b by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModal: MainViewModal by viewModels()

    private val adap by lazy { TaskAdap(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)

        b.rvTasks.adapter = adap
        viewModal.tasksRv.observe(this){
            adap.submitList(it)
            Log.d("TAG--", "onCreate: ${it.size}")
        }

    }

    override fun onClick(pos: Int) {
        Log.d("TAG--", "onClick: $pos")
    }
}