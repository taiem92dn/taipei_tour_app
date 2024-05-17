package com.taingdev.taipeitourapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.FragmentDetailBinding
import com.taingdev.taipeitourapp.extensions.makeLinks
import com.taingdev.taipeitourapp.util.EXTRA_URL


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

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

        args.extraAttraction.also {
            binding.toolbar.title = it.name
            binding.attraction = it
            binding.executePendingBindings()
        }

        bindEvents()
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