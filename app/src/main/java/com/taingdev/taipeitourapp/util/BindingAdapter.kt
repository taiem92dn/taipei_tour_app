package com.taingdev.taipeitourapp.util

import android.os.Build
import android.text.Html
import android.text.util.Linkify
import android.widget.ImageView
import android.widget.TextView
import com.taingdev.taipeitourapp.model.Attraction
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.taingdev.taipeitourapp.R
import timber.log.Timber

object BindingAdapter {

    @BindingAdapter("attractionImage")
    @JvmStatic
    fun loadAttractionImage(imageView: ImageView, attraction: Attraction) {
        Glide.with(imageView).load(
            attraction.images.let {
                if (it.isNotEmpty()) {
                    it[0].src
                } else {
                    null
                }
            }.apply {
                Timber.d("attraction.images: $this")
            }
        )
            .placeholder(R.drawable.ic_default_image)
            .into(imageView)
    }

    @BindingAdapter("htmlText")
    @JvmStatic
    fun loadHtmlText(textView: TextView, htmlText: String) {
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlText)
        }
    }
}