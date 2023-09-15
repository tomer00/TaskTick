package com.tomer.tasktick.adap

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tomer.tasktick.R
import com.tomer.tasktick.modals.Task

class TaskAdap(private val clickLis: CallbackClick) : ListAdapter<Task, TaskAdap.SamHolder>(TaskUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        val b = com.tomer.tasktick.databinding.TaskRowBinding.bind(v)
        return SamHolder(b, clickLis)
    }

    override fun onBindViewHolder(holder: SamHolder, position: Int) {
        holder.b.apply {
            bg.setCol(
                (
                        when (getItem(position).priority) {
                            1 -> Color.GREEN
                            2 -> Color.YELLOW
                            else -> Color.RED
                        }
                        )
            )
            tvTask.text = currentList[position].des
        }
    }

    interface CallbackClick {
        fun onClick(pos: Int)
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