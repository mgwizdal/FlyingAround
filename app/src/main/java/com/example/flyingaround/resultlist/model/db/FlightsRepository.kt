package com.example.flyingaround.resultlist.model.db

import com.example.flyingaround.details.view.DetailsItem
import com.example.flyingaround.resultlist.view.FlightInfoItem
import io.reactivex.Flowable

class FlightsRepository(
    private val flightsDao: FlightsDao
) {

    fun insertFlightInfoItems(flightInfoItemEntityList: List<FlightInfoItemEntity>) {
        flightsDao.insertFlyingItems(*flightInfoItemEntityList.toTypedArray())
    }

    fun getFlights(): Flowable<List<FlightInfoItem>> {
        return flightsDao.getAllFlightInfoItems()
            .map { list ->
                list.map {
                    FlightInfoItem(
                        it.flightDate,
                        it.flightNumber,
                        it.duration,
                        it.regularFarePrice,
                        it.currency
                    )
                }
            }
    }

    fun getFlightInfo(flightNumber: String): Flowable<DetailsItem> {
        return flightsDao.getFlightInfoItem(flightNumber)
            .map {
                DetailsItem(
                    it.origin,
                    it.destination,
                    it.infantsLeft,
                    it.fareClass,
                    it.discountInPercent
                )
            }
    }

    fun deleteCurrentItems() {
        flightsDao.deleteCurrentItems()
    }
}