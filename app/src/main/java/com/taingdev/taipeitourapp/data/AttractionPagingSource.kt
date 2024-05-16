package com.taingdev.taipeitourapp.data

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taingdev.taipeitourapp.R
import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.network.INetworkCheckService
import com.taingdev.taipeitourapp.repository.AttractionRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1

class AttractionPagingSource(
    private val dataSource: AttractionDataSource,
    private val application: Application,
    private val networkCheckService: INetworkCheckService
): PagingSource<Int, Attraction>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Attraction> {
        val position = params.key ?: STARTING_PAGE_INDEX

        if (!networkCheckService.hasInternet())
            return LoadResult.Error(Exception(application.getString(R.string.no_internet)))

        return try {
            val response = dataSource.fetchAllAttractions(position)
            val list = response
            val nextKey = if (list.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = list,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Attraction>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}