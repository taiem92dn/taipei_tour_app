package com.taingdev.taipeitourapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.ListLoadStateFooterViewItemBinding

class ItemsLoadStateViewHolder(
    private val binding: ListLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ItemsLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_load_state_footer_view_item, parent, false)
            val binding = ListLoadStateFooterViewItemBinding.bind(view)
            return ItemsLoadStateViewHolder(binding, retry)
        }
    }
}