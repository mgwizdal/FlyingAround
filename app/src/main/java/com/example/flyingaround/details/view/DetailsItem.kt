package com.example.flyingaround.details.view

data class DetailsItem(
    val origin: String,
    val destination: String,
    val infantsLeft: Int,
    val fareClass: String,
    val discountInPercent: Int
)