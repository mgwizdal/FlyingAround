package com.example.flyingaround.resultlist.model.network

data class FlightDto(
    val flightNumber: String,
    val timeUTC: List<String>,
    val duration: String,
    val infantsLeft: Int,
    val regularFare: RegularFareDto
)
