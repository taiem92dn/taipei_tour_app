package com.taingdev.taipeitourapp.data

import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.network.AttractionService

class RemoteAttractionDataSource constructor(
    private val attractionService: AttractionService
): AttractionDataSource {
    override suspend fun fetchAllAttractions(page: Int): List<Attraction>
    = attractionService.fetchAllAttractions(page).data

}