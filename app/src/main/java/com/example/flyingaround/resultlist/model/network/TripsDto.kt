package com.example.flyingaround.resultlist.model.network

data class TripsDto(
    val origin: String,
    val originName: String,
    val destination: String,
    val destinationName: String,
    val dates: List<TripDatesDto>
)
