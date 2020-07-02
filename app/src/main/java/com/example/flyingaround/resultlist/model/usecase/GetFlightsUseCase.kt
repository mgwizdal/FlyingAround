package com.example.flyingaround.resultlist.model.usecase

import android.util.Log
import com.example.flyingaround.resultlist.model.db.FlightsRepository
import com.example.flyingaround.resultlist.model.network.*
import com.example.flyingaround.utils.toBodyOrError
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import retrofit2.Response

class GetFlightsUseCase(
    private val getFlightsService: GetFlightsService,
    private val flightsRepository: FlightsRepository
) : ObservableTransformer<GetFlightsAction, GetFlightsResult> {
    override fun apply(upstream: Observable<GetFlightsAction>): ObservableSource<GetFlightsResult> {
        return upstream
            .map {
                flightsRepository.deleteCurrentItems()
                it
            }
            .flatMap { action ->
                getFlightsService.getFlights(
                    origin = action.origin,
                    destination = action.destination,
                    dateOut = action.departureDate,
                    flexDaysOut = FLEX_DAYS_OUT,
                    adults = action.adults.toString(),
                    teens = action.teens.toString(),
                    chd = action.children.toString(),
                    roundTrip = false,
                    toUs = TO_US
                ).toObservable()
                    .map { it.toBodyOrError() }
                    .map {
                        val flightInfoItemEntities = it.getFlyingItemEntities()
                        flightsRepository.insertFlightInfoItems(flightInfoItemEntities)
                        flightInfoItemEntities.isEmpty()
                    }
                    .map { GetFlightsResult.Success(it) as GetFlightsResult }
                    .onErrorReturn { GetFlightsResult.Error(it) }
            }
    }

    companion object {
        private const val TO_US = "AGREED"
        private const val FLEX_DAYS_OUT = "3"
    }
}

data class GetFlightsAction(
    val origin: String,
    val destination: String,
    val departureDate: String,
    val adults: Int,
    val teens: Int,
    val children: Int
)

sealed class GetFlightsResult {
    data class Success(val isEmptyList: Boolean) : GetFlightsResult()
    data class Error(val throwable: Throwable) : GetFlightsResult()
}

