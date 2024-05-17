package com.taingdev.taipeitourapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.LayoutImgSliderItemBinding
import java.util.Objects

class ImageViewPagerAdapter(
    val imageUrls: List<String>
) : PagerAdapter() {
    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as FrameLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = LayoutImgSliderItemBinding.inflate(inflater, container, false)

        Glide.with(binding.imageView)
            .load(imageUrls[position])
            .placeholder(R.drawable.ic_default_image)
            .into(binding.imageView)

        Objects.requireNonNull(container).addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }
}