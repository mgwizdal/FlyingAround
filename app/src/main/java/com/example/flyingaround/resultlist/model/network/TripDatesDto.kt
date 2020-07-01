package com.example.flyingaround.resultlist.model.network

data class TripDatesDto(
    val dateOut: String,
    val flights: List<FlightDto>
)
