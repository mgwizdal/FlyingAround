package com.example.flyingaround.resultlist.model.network

import com.example.flyingaround.search.model.network.StationContainerDto
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetFlightsService {
    @GET("https://sit-nativeapps.ryanair.com/api/v3/Availability")
    fun getFlights(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("dateout") dateOut: String,
        @Query("flexdaysout") flexDaysOut: String,
        @Query("adt") adults: String,
        @Query("teen") teens: String,
        @Query("chd") chd: String,
        @Query("roundtrip") roundTrip: Boolean,
        @Query("ToUs") toUs: String
    ): Single<Response<SearchResultDto>>
}