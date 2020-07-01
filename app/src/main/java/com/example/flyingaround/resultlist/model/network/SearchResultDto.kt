package com.example.flyingaround.resultlist.model.network

import com.example.flyingaround.resultlist.model.db.FlightInfoItemEntity

data class SearchResultDto(
    val currency: String,
    val trips: List<TripsDto>
) {
    fun getFlyingItemEntities(): List<FlightInfoItemEntity> {
        val list = mutableListOf<FlightInfoItemEntity>()
        trips.forEach { trip ->
            trip.dates.forEach { date ->
                date.flights.forEach { flight ->
                    flight.regularFare.fares.forEach { fare ->
                        list.add(
                            FlightInfoItemEntity(
                                trip.originName,
                                trip.destinationName,
                                flight.timeUTC.first(),
                                flight.flightNumber,
                                flight.duration,
                                fare.publishedFare,
                                flight.infantsLeft,
                                flight.regularFare.fareClass,
                                fare.discountInPercent,
                                currency
                            )
                        )
                    }
                }
            }
        }
        return list
    }
}