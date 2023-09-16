package com.tomer.tasktick.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.tomer.tasktick.adap.TaskAdap
import com.tomer.tasktick.databinding.ActivityMainBinding
import com.tomer.tasktick.ui.views.SwipeHelper
import com.tomer.tasktick.viewmodals.MainViewModal
import dagger.hilt.android.AndroidEntryPoint


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
        }

    }

    override fun onClick(pos: Int) {
        Log.d("TAG--", "onClick: $pos")
    }

    override fun onDone(pos: Int) {
        Log.d("TAG--", "onDone: $pos")
    }
}