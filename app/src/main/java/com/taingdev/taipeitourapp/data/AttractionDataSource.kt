package com.taingdev.taipeitourapp.data

import com.taingdev.taipeitourapp.model.Attraction

interface AttractionDataSource {

    suspend fun fetchAllAttractions(
        page: Int,
    ): List<Attraction>
}