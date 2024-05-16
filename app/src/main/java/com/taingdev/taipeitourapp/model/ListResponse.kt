package com.taingdev.taipeitourapp.model

import com.google.gson.annotations.SerializedName

data class ListResponse<T>(
    val data: List<T> = emptyList(),
    val total: Int,
)
