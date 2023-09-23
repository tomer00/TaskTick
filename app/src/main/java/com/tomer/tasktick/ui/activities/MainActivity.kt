package com.tomer.tasktick.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.gson.Gson
import com.tomer.tasktick.R
import com.tomer.tasktick.adap.TaskAdap
import com.tomer.tasktick.databinding.ActivityMainBinding
import com.tomer.tasktick.modals.Task
import com.tomer.tasktick.ui.views.SwipeHelper
import com.tomer.tasktick.viewmodals.MainViewModal
import com.tomer.tasktick.viewmodals.TaskViewModal
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskAdap.CallbackClick {

    private val b by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModal: MainViewModal by viewModels()

    @Inject
    lateinit var gson: Gson

    private val taskContract = object : ActivityResultContract<Unit, Task?>() {
        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(this@MainActivity, TaskActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Task? {
            if (resultCode == RESULT_OK) {
                return gson.fromJson(intent!!.getStringExtra("task").toString(), Task::class.java)
            }
            return null
        }
    }

    private val taskL = registerForActivityResult(taskContract) {
        if (it != null) {
            viewModal.saveTask(it)
        }
    }

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

        viewModal.currentTask.observe(this) {
            b.placeHOlder.visibility = View.GONE
            b.currentTask.root.visibility = View.VISIBLE
            b.tvUpTask.visibility = View.VISIBLE
            b.currentTask.tvTask.text = URLDecoder.decode(it.des, "UTF-8")
            b.currentTask.tvTimeStarts.text = TaskViewModal.timeStart(it.timeCreated)
        }

        //endregion ::OBSERVERS

        b.apply {
            snackBar.y = resources.displayMetrics.heightPixels + 10f
            btUndo.setOnClickListener {
                viewModal.undo()
            }
            btAddTask.setOnClickListener {
                startTaskActivity()
            }
            b.currentTask.lottie.visibility  = View.GONE
        }

        val c = Calendar.getInstance()
        b.tvDateDay.text = c.get(Calendar.DAY_OF_MONTH).toString()
        b.tvDateYear.text = c.get(Calendar.YEAR).toString()
        b.tvDateMonth.text = TaskViewModal.months[c.get(Calendar.MONTH)]

    }

    override fun onClick(pos: Int) {
        Toast.makeText(this, "${adap.currentList[pos].id}", Toast.LENGTH_SHORT).show()
    }

    override fun onDone(pos: Int) {
        viewModal.updateTask(pos)
        b.root.postDelayed({
            adap.currentList[pos].isDone = true
            adap.notifyItemChanged(pos)
        },1000)
    }

    private fun startTaskActivity() {
        taskL.launch(Unit)
        overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
    }
}