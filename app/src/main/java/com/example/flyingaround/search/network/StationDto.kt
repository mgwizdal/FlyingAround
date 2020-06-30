package com.example.flyingaround.search.network

data class StationDto(
    val code: String,
    val name: String,
    val alternateName: String?,
    val countryCode: String,
    val countryName: String
)
