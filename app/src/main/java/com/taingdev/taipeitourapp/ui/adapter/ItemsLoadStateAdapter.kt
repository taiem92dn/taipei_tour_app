package com.taingdev.taipeitourapp.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ItemsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ItemsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ItemsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ItemsLoadStateViewHolder {
        return ItemsLoadStateViewHolder.create(parent, retry)
    }
}