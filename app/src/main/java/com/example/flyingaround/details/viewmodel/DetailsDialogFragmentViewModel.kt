package com.example.flyingaround.details.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flyingaround.details.view.DetailsItem
import com.example.flyingaround.resultlist.model.db.FlightsRepository
import example.mobile.engie.com.capfiszki.utils.RxSchedulers
import io.reactivex.Flowable
import io.reactivex.Observable

class DetailsDialogFragmentViewModel(
    private val flightsRepository: FlightsRepository,
    private val rxSchedulers: RxSchedulers
) : ViewModel() {

    fun getFlightInfo(flightNumber: String): Observable<DetailsItem> {
        return flightsRepository.getFlightInfo(flightNumber)
            .toObservable()
            .subscribeOn(rxSchedulers.io)
            .observeOn(rxSchedulers.ui)
    }
}