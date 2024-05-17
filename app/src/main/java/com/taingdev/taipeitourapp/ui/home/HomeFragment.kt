package com.taingdev.taipeitourapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.google.android.material.transition.MaterialFadeThrough
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.databinding.FragmentHomeBinding
import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.ui.adapter.AttractionAdapter
import com.taingdev.taipeitourapp.ui.adapter.ItemsLoadStateAdapter
import com.taingdev.taipeitourapp.util.EXTRA_ATTRACTION
import com.taingdev.taipeitourapp.util.PrefUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), MenuProvider {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AttractionListViewModel by viewModels()

    private var adapter: AttractionAdapter? = null

    @Inject
    lateinit var prefUtil: PrefUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.appBarLayout.toolbar)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)

        enterTransition = MaterialFadeThrough().addTarget(binding.rvList)
        reenterTransition = MaterialFadeThrough().addTarget(binding.rvList)

        initAdapter()
        bindEvents()
        bindData()

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
        findNavController().navigate(
            R.id.DetailFragment,
            bundleOf(EXTRA_ATTRACTION to attraction),
            null
        )
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                pagingData.collectLatest(attractionAdapter::submitData)
            }
        }

        lifecycleScope.launch {
            attractionAdapter.loadStateFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                .collect { loadState ->
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
                        "\uD83D\uDE28 Wooops ${it.error.message}",
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)

        val langItem: MenuItem = menu.findItem(R.id.action_language)
        setUpLangMenu(langItem.subMenu)
    }

    private fun setUpLangMenu(subMenu: SubMenu?) {
        when (prefUtil.getLanguage()) {
            "zh-tw" -> subMenu?.findItem(R.id.action_lang_zh_tw)?.isChecked = true
            "zh-cn" -> subMenu?.findItem(R.id.action_lang_zh_cn)?.isChecked = true
            "en" -> subMenu?.findItem(R.id.action_lang_en)?.isChecked = true
            "ja" -> subMenu?.findItem(R.id.action_lang_ja)?.isChecked = true
            "ko" -> subMenu?.findItem(R.id.action_lang_ko)?.isChecked = true
            "es" -> subMenu?.findItem(R.id.action_lang_es)?.isChecked = true
            "id" -> subMenu?.findItem(R.id.action_lang_id)?.isChecked = true
            "th" -> subMenu?.findItem(R.id.action_lang_th)?.isChecked = true
            "vi" -> subMenu?.findItem(R.id.action_lang_vi)?.isChecked = true
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (handleLanguageMenuItem(menuItem)) {
            return true
        }

        return false
    }

    private fun handleLanguageMenuItem(menuItem: MenuItem): Boolean {
        val language = when (menuItem.itemId) {
            R.id.action_lang_zh_tw -> "zh-tw"
            R.id.action_lang_zh_cn -> "zh-cn"
            R.id.action_lang_en -> "en"
            R.id.action_lang_ja -> "ja"
            R.id.action_lang_ko -> "ko"
            R.id.action_lang_es -> "es"
            R.id.action_lang_id -> "id"
            R.id.action_lang_th -> "th"
            R.id.action_lang_vi -> "vi"
            else -> null
        }

        if (language != null) {
            menuItem.isChecked = true
            setAndSaveLanguage(language)
            return true
        }

        return false
    }

    private fun setAndSaveLanguage(language: String) {
        prefUtil.saveLanguage(language)
        refreshData()
    }
}