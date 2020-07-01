package com.example.flyingaround.resultlist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flyingaround.resultlist.model.db.FlightsRepository
import com.example.flyingaround.resultlist.model.usecase.GetFlightsAction
import com.example.flyingaround.resultlist.model.usecase.GetFlightsResult
import com.example.flyingaround.resultlist.model.usecase.GetFlightsUseCase
import com.example.flyingaround.resultlist.view.FlightInfoItem
import example.mobile.engie.com.capfiszki.utils.RxSchedulers
import io.reactivex.Flowable
import io.reactivex.Observable

class ResultListActivityViewModel(
    private val getFlightsUseCase: GetFlightsUseCase,
    flightsRepository: FlightsRepository,
    private val rxSchedulers: RxSchedulers
) : ViewModel() {

    val listOfFlightItems: Flowable<List<FlightInfoItem>> = flightsRepository.getFlights()
        .subscribeOn(rxSchedulers.io)
        .observeOn(rxSchedulers.ui)

    fun downloadFlights(origin: String, destination: String, departureDate: String, adults: Int, teens: Int, children: Int): Observable<GetFlightsResult> {
        return Observable.just(GetFlightsAction(origin, destination, departureDate, adults, teens, children))
            .observeOn(rxSchedulers.io)
            .compose(getFlightsUseCase)
            .observeOn(rxSchedulers.ui)
    }

}