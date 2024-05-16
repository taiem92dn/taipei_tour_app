package com.taingdev.taipeitourapp.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.taingdev.taipeitourapp.data.AttractionDataSource
import com.taingdev.taipeitourapp.data.AttractionPagingSource
import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.network.INetworkCheckService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttractionRepository @Inject constructor(
    private val application: Application,
    private val attractionDataSource: AttractionDataSource,
    private val networkCheckService: INetworkCheckService
) {

    fun getAttractionPaging(): Flow<PagingData<Attraction>> = Pager(
       config = PagingConfig(
           pageSize = NETWORK_PAGE_SIZE,
           enablePlaceholders = false
       ),
        pagingSourceFactory = {
            AttractionPagingSource(
                attractionDataSource, application, networkCheckService
            )
        }
    ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 500
    }
}