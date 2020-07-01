package com.example.flyingaround.search.model.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface SearchService {
    @GET("https://tripstest.ryanair.com/static/stations.json")
    fun getAirports(): Single<Response<StationContainerDto>>
}