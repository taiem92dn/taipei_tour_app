package com.taingdev.taipeitourapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialFadeThrough
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.FragmentDetailBinding
import com.taingdev.taipeitourapp.extensions.makeLinks
import com.taingdev.taipeitourapp.ui.adapter.ImageViewPagerAdapter
import com.taingdev.taipeitourapp.util.EXTRA_URL


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private val attractionArg get() = args.extraAttraction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        enterTransition = MaterialFadeThrough().addTarget(binding.appBarLayout)
        reenterTransition = MaterialFadeThrough().addTarget(binding.appBarLayout)

        args.extraAttraction.also {
            binding.toolbar.title = it.name
            binding.attraction = it
            binding.executePendingBindings()
        }

        initAdapter()
        bindEvents()
    }

    private fun initAdapter() {
        binding.apply {
            val adapter = ImageViewPagerAdapter(
                attractionArg.images.map { it.src }
            )
            imagesViewPager.adapter = adapter

            indicator.setViewPager(imagesViewPager)
            adapter.registerDataSetObserver(indicator.dataSetObserver)
        }

    }

    private fun bindEvents() {
        val attraction = args.extraAttraction
        if (attraction.officialSite.isNotEmpty()) {
            binding.tvOfficialSite.makeLinks(
                Pair(attraction.officialSite, View.OnClickListener {
                    navigateToWebView(attraction.officialSite)
                })
            )
        }
    }

    private fun navigateToWebView(url: String) {
        findNavController().navigate(
            R.id.WebViewFragment,
            bundleOf(EXTRA_URL to url),
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}