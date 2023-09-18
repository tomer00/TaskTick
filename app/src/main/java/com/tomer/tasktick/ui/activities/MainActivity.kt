package com.tomer.tasktick.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.ItemTouchHelper
import com.tomer.tasktick.R
import com.tomer.tasktick.adap.TaskAdap
import com.tomer.tasktick.databinding.ActivityMainBinding
import com.tomer.tasktick.ui.views.SwipeHelper
import com.tomer.tasktick.viewmodals.MainViewModal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskAdap.CallbackClick {

    private val b by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModal: MainViewModal by viewModels()

    private val adap by lazy { TaskAdap(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)

        b.rvTasks.adapter = adap
        ItemTouchHelper(SwipeHelper(this) { pos ->
            if (pos != -1)
                viewModal.removeFromRv(adap.currentList[pos].id)

        }).apply {
            attachToRecyclerView(b.rvTasks)
        }

        //region ::OBSERVERS

        viewModal.tasksRv.observe(this) {
            adap.submitList(it)
            Log.d("TAG--", "onCreate: ${it.size}")
        }

        viewModal.snack.observe(this) {
            if (it) {
                b.btUndo.isClickable = true
                b.snackBar.animate().apply {
                    y(resources.displayMetrics.heightPixels - b.snackBar.height.toFloat())
                    duration = 200
                    start()
                }
            } else {
                b.btUndo.isClickable = false
                b.snackBar.animate().apply {
                    yBy(b.snackBar.height * 2 + 10f)
                    duration = 200
                    start()
                }
            }
        }

        //endregion ::OBSERVERS

        b.apply {
            snackBar.y = resources.displayMetrics.heightPixels + 10f
            btUndo.setOnClickListener {
                viewModal.undo()
            }
            btAddTask.setOnClickListener {
                val i = Intent(this@MainActivity,TaskActivity::class.java)
                startActivity(i)
            }
        }

    }

    override fun onClick(pos: Int) {
        Log.d("TAG--", "onClick: $pos")
    }

    override fun onDone(pos: Int) {
        Log.d("TAG--", "onDone: $pos")
    }
}