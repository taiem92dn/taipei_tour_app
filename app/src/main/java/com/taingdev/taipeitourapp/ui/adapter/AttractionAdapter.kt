package com.taingdev.taipeitourapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.taingdev.taipeitourapp.databinding.ItemAttractionListBinding
import com.taingdev.taipeitourapp.model.Attraction

class AttractionAdapter : PagingDataAdapter<Attraction, ViewHolder>(DIFF_CALLBACK) {

    var onItemClickListener: (Attraction) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttractionListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AttractionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is AttractionViewHolder -> {
                getItem(position)?.let { holder.bind(it) }
            }
        }
    }

    inner class AttractionViewHolder(val binding: ItemAttractionListBinding) : ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {item ->
                    onItemClickListener(item)
                }
            }

        }

        fun bind(attraction: Attraction) {
            binding.also {
                it.attraction = attraction
                it.executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Attraction>() {
            override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
                return oldItem == newItem
            }

        }
    }
}