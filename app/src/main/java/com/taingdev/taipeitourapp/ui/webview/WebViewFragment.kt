package com.taingdev.taipeitourapp.ui.webview

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.FragmentWebviewBinding

class WebViewFragment: Fragment() {

    private var _binding: FragmentWebviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args by navArgs<WebViewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.appBarLayout.title = getString(R.string.txt_loading)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.appBarLayout.toolbar)

        val url = args.extraUrl
        binding.apply {
//            webView.settings.javaScriptEnabled = true
            // Handling Page Navigation
            webView.webViewClient = WebViewClient()

            webView.webChromeClient = object: WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    _binding?.appBarLayout?.title = title.toString()
                }
            }
            webView.loadUrl(url)
        }

        binding.webView.isFocusableInTouchMode = true
        binding.webView.requestFocus()
        binding.webView.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
                binding.webView.goBack()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}