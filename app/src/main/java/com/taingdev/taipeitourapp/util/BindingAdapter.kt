package com.taingdev.taipeitourapp.util

import android.widget.ImageView
import com.taingdev.taipeitourapp.model.Attraction
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import timber.log.Timber

object BindingAdapter {

    @BindingAdapter("attractionImage")
    @JvmStatic
    fun loadAttractionImage(imageView: ImageView, attraction: Attraction) {
        Glide.with(imageView).load(
            attraction.images.let {
                if (it.isNotEmpty()) {
                    it[0].src
                }
                else {
                    null
                }
            }.apply {
                Timber.d("attraction.images: $this")
            }
        ).into(imageView)
    }
}