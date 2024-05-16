package com.taingdev.taipeitourapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AttractionListViewModel @Inject constructor(
    private val attractionRepository: AttractionRepository
) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val showError = MutableLiveData<Boolean>(false)

    val pagingDataFlow: Flow<PagingData<Attraction>>

    init {
        pagingDataFlow = getAttractionList()
    }

    fun setShowError(message: String) {
        errorMessage.value = message
        showError.value = true
    }

    fun hideError() {
        showError.value = false
    }

    private fun getAttractionList(): Flow<PagingData<Attraction>> =
        attractionRepository.getAttractionPaging()

    companion object {
        val TAG = AttractionListViewModel::class.simpleName
    }
}