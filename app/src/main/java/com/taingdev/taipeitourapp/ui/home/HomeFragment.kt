package com.taingdev.taipeitourapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.FragmentHomeBinding
import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.ui.adapter.AttractionAdapter
import com.taingdev.taipeitourapp.ui.adapter.ItemsLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AttractionListViewModel by viewModels()

    private var adapter: AttractionAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        bindEvents()
        bindData()

        refreshData()

        binding.appBarLayout.title = getString(R.string.app_name)
    }

    private fun refreshData() {
        adapter?.refresh()
    }

    private fun bindData() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            showError = viewModel.showError
            errorMessage = viewModel.errorMessage
        }
    }

    private fun bindEvents() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        binding.layoutProgressBar.btRetry.setOnClickListener {
            viewModel.hideError()
            adapter?.retry()
        }

        adapter?.onItemClickListener = {
            navigateToDetail(it)
        }
    }

    private fun navigateToDetail(attraction: Attraction) {
    }

    private fun initAdapter() {
        adapter = AttractionAdapter()
        binding.rvList.also {
            it.adapter = adapter?.withLoadStateHeaderAndFooter(
                header = ItemsLoadStateAdapter { adapter?.retry() },
                footer = ItemsLoadStateAdapter { adapter?.retry() }
            )
            it.setHasFixedSize(true)
        }
        binding.bindList(adapter!!, viewModel.pagingDataFlow)
    }

    private fun FragmentHomeBinding.bindList(
        attractionAdapter: AttractionAdapter,
        pagingData: Flow<PagingData<Attraction>>,
    ) {
        lifecycleScope.launch {
            pagingData.collectLatest(attractionAdapter::submitData)
        }

        lifecycleScope.launch {
            attractionAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && attractionAdapter.itemCount == 0
                // show empty list
                if (isListEmpty) {
                    viewModel.setShowError(getString(R.string.no_results))
                }
                // Only show the list if refresh succeeds.
                rvList.isVisible = !isListEmpty
                // Show loading spinner during initial load or refresh.
                if (loadState.source.refresh is LoadState.Loading) {
                    showLoading()
                } else {
                    hideLoading()
                }

                // Show the retry state if initial load or refresh fails.
                if (loadState.source.refresh is LoadState.Error
                    && attractionAdapter.itemCount == 0
                ) {
                    viewModel.setShowError(
                        (loadState.source.refresh as LoadState.Error).error.message ?: ""
                    )
                }

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.source.refresh as? LoadState.Error

                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun showLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun hideLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}