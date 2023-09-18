package com.tomer.tasktick.ui.activities

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.ViewAnimationUtils
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.indices
import com.tomer.tasktick.R
import com.tomer.tasktick.databinding.ActivityTaskBinding
import com.tomer.tasktick.viewmodals.TaskViewModal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    //region :: GLOBALS-->>

    private val b by lazy { ActivityTaskBinding.inflate(layoutInflater) }
    private val vm: TaskViewModal by viewModels()

    //endregion :: GLOBALS-->>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        if (intent.hasExtra("task")) {
            vm.setUI(intent.getStringExtra("task").toString())
        }

        b.apply {

            btBack.setOnClickListener {
                setResult(RESULT_CANCELED)
                super.onBackPressed()
            }
            btPriorMed.setOnClickListener(vm.cli)
            btPriorHigh.setOnClickListener(vm.cli)
            btPriorLow.setOnClickListener(vm.cli)
            btAddTask.setOnClickListener {
                if (b.etTaskDes.text.isEmpty()) {
                    b.etTaskDes.error = "Please type your Task..."
                    return@setOnClickListener
                }
                vm.setTaskDes(b.etTaskDes.text.toString())
            }
            etTaskDes.setText(vm.textEt)
        }

        vm.dates.observe(this) {
            b.daysLL.getChildAt(0).apply {
                setOnClickListener(vm.daysCli)
                tag = 0
            }
            b.daysLL.getChildAt(1).apply {
                setOnClickListener(vm.daysCli)
                tag = 1
            }
            var t = 2
            it.forEach { date ->
                val v = layoutInflater.inflate(R.layout.time_row, b.daysLL, false) as TextView
                v.text = date
                v.tag = t++
                v.setOnClickListener(vm.daysCli)
                b.daysLL.addView(v)
            }
            t = 0
            vm.listTime.forEach { time ->
                val v = layoutInflater.inflate(R.layout.time_row, b.daysLL, false) as TextView
                v.text = time
                v.tag = t++
                v.setOnClickListener(vm.timeCli)
                b.timeLL.addView(v)
            }
            b.timeLL[4].background = ContextCompat.getDrawable(this, R.drawable.selected_bg)
        }

        vm.day.observe(this) { ii ->
            for (i in b.daysLL.indices) {
                if (ii == i) {
                    b.daysLL.getChildAt(i).background = ContextCompat.getDrawable(this, R.drawable.selected_bg)
                } else b.daysLL.getChildAt(i).background = ContextCompat.getDrawable(this, R.drawable.et_bg)
            }
        }
        vm.time.observe(this) { ii ->
            for (i in b.timeLL.indices) {
                if (ii == i) {
                    b.timeLL.getChildAt(i).background = ContextCompat.getDrawable(this, R.drawable.selected_bg)
                } else b.timeLL.getChildAt(i).background = ContextCompat.getDrawable(this, R.drawable.et_bg)
            }
        }
        vm.prior.observe(this) {
            if (it == 1) {//low
                b.btPriorLow.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.selected_bg)
                b.btPriorMed.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.et_bg)
                b.btPriorHigh.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.et_bg)
            } else if (it == 2) {//medium
                b.btPriorMed.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.selected_bg)
                b.btPriorLow.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.et_bg)
                b.btPriorHigh.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.et_bg)
            } else {//high
                b.btPriorHigh.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.selected_bg)
                b.btPriorLow.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.et_bg)
                b.btPriorMed.background = ContextCompat.getDrawable(this@TaskActivity, R.drawable.et_bg)
            }
        }

        vm.finalTask.observe(this) {
            val i = Intent().apply {
                putExtra("task", it)
            }
            setResult(RESULT_OK, i)
            finish()
        }


        vm.isNew.observe(this) {
            b.topHead.text = if (it) "Add Task"
            else "Update Task"
        }

    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        val off = 52 * resources.displayMetrics.density
        ViewAnimationUtils.createCircularReveal(
            b.root, (resources.displayMetrics.widthPixels - off).toInt(),
            (resources.displayMetrics.heightPixels - off).toInt(), 0f,
            resources.displayMetrics.heightPixels.toFloat()
        ).apply {
            duration = 440
        }.start()

        ObjectAnimator.ofObject(
            b.root, "backgroundColor", ArgbEvaluator(),
            ContextCompat.getColor(this, R.color.primary_dark), ContextCompat.getColor(this, R.color.backgroundLight)
        ).apply {
            duration = 400
        }.start()
    }

}