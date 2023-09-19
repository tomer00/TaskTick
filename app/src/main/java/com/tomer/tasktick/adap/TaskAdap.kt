package com.tomer.tasktick.adap

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.airbnb.lottie.LottieAnimationView
import com.tomer.tasktick.R
import com.tomer.tasktick.modals.Task
import com.tomer.tasktick.viewmodals.TaskViewModal
import java.net.URLDecoder

class TaskAdap(private val clickLis: CallbackClick) : ListAdapter<Task, TaskAdap.SamHolder>(TaskUtil()) {

    private val cli = View.OnClickListener {
        (it as LottieAnimationView).playAnimation()
        clickLis.onDone(it.tag as Int)
        it.setOnClickListener(null)
        it.isClickable = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        val b = com.tomer.tasktick.databinding.TaskRowBinding.bind(v)
        return SamHolder(b, clickLis)
    }

    override fun onBindViewHolder(holder: SamHolder, position: Int) {
        holder.b.apply {
            val item = getItem(position)
            bg.setCol(
                (
                        when (item.priority) {
                            1 -> Color.GREEN
                            2 -> Color.YELLOW
                            else -> Color.RED
                        }
                        )
            )
            tvTask.text = URLDecoder.decode(currentList[position].des, "UTF-8")
            tvTimeStarts.text = TaskViewModal.timeStart(item.timeCreated)
            if (item.isDone) {
                tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                bg.setCol(ContextCompat.getColor(this.root.context, R.color.gray))
                lottie.progress = 1f
                tvTimeStarts.append(" Completed at ${TaskViewModal.timeStart(item.timeDone)}")
            } else {
                lottie.tag = position
                lottie.setOnClickListener(cli)
                lottie.progress = 0f
                tvTask.paintFlags = 0
            }
        }
    }

    interface CallbackClick {
        fun onClick(pos: Int)
        fun onDone(pos: Int)
    }

    inner class SamHolder(val b: com.tomer.tasktick.databinding.TaskRowBinding, clickLis: CallbackClick) : ViewHolder(b.root) {
        init {
            b.root.setOnClickListener {
                clickLis.onClick(adapterPosition)
            }
        }
    }

    class TaskUtil : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.isDone == newItem.isDone
    }
}