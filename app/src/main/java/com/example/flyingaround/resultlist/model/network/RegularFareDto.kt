package com.example.flyingaround.resultlist.model.network

data class RegularFareDto(
    val fareKey: String,
    val fareClass: String,
    val fares: List<FareDto>
)
