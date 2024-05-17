package com.taingdev.taipeitourapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attraction(
    val id: Int,
    val name: String,
    @SerializedName("open_status") val openStatus: Int,
    @SerializedName("official_site") val officialSite: String,
    val introduction: String,
    @SerializedName("open_time") val openTime: String,
    val address: String,
    val url: String,
    val modified: String,
    val images: List<ImageItem>
): Parcelable

@Parcelize
data class ImageItem (
    val src: String,
    val subject: String,
    val ext: String
): Parcelable
