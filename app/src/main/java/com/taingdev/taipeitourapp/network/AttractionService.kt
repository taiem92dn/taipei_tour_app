package com.taingdev.taipeitourapp.network

import com.taingdev.taipeitourapp.model.Attraction
import com.taingdev.taipeitourapp.model.ListResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AttractionService {

    @Headers("Accept: application/json")
    @GET("Attractions/All")
    suspend fun fetchAllAttractions(
        @Query("page") page: Int,
    ): ListResponse<Attraction>
}