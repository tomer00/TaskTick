package com.tomer.tasktick.adap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tomer.tasktick.R
import com.tomer.tasktick.databinding.SamRowBinding
import com.tomer.tasktick.modals.SamModal

class SampleAdap(private val clickLis: CallbackClick) : ListAdapter<SamModal, SampleAdap.SamHolder>(SamUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sam_row,parent,false)
        val b = SamRowBinding.bind(v)
        return SamHolder(b,clickLis)
    }

    override fun onBindViewHolder(holder: SamHolder, position: Int) {

    }

    interface CallbackClick {
        fun onClick(pos: Int)
    }

    inner class SamHolder(b: SamRowBinding, clickLis: CallbackClick) : ViewHolder(b.root) {}

    class SamUtil : DiffUtil.ItemCallback<SamModal>() {
        override fun areItemsTheSame(oldItem: SamModal, newItem: SamModal) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SamModal, newItem: SamModal) = oldItem.id == newItem.id
    }
}