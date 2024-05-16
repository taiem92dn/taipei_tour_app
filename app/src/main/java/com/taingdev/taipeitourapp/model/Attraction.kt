package com.taingdev.taipeitourapp.model

import com.google.gson.annotations.SerializedName

data class Attraction(
    val id: Int,
    val name: String,
    @SerializedName("open_status") val openStatus: Int,
    val introduction: String,
    @SerializedName("open_time") val openTime: String,
    val address: String,
    val url: String,
    val modified: String,
    val images: List<ImageItem>
)

data class ImageItem (
    val src: String,
    val subject: String,
    val ext: String
)
