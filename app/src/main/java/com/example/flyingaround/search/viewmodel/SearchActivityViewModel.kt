package com.example.flyingaround.search.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flyingaround.search.model.db.StationRepository
import com.example.flyingaround.search.model.usecase.GetAirportsAction
import com.example.flyingaround.search.model.usecase.GetAirportsResult
import com.example.flyingaround.search.model.usecase.GetAirportsUseCase
import com.example.flyingaround.search.view.StationAutoComplete
import com.example.flyingaround.utils.include
import example.mobile.engie.com.capfiszki.utils.RxSchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class SearchActivityViewModel(
    private val getAirportsUseCase: GetAirportsUseCase,
    private val stationRepository: StationRepository,
    private val rxSchedulers: RxSchedulers
) : ViewModel() {
    private val clearDisposables = CompositeDisposable()
    private val initializationSubject = PublishSubject.create<InitializationUiState>()
    val initializationObservable: Observable<InitializationUiState> = initializationSubject
        .startWith(InitializationUiState.Loading)
        .observeOn(rxSchedulers.ui)

    val validationSubject = PublishSubject.create<ValidationContent>()
    val validationObservable: Observable<Validation> = validationSubject
        .subscribeOn(rxSchedulers.disk)
        .observeOn(rxSchedulers.disk)
        .map(::validateForm)
        .map(::mapToResult)
        .observeOn(rxSchedulers.ui)

    fun search(originStation: String, destination: String, departureTime: String, adults: String) {
        validationSubject.onNext(
            ValidationContent(
                originStation,
                destination,
                departureTime,
                adults
            )
        )
    }

    fun initialize() {
        clearDisposables include Observable.just(GetAirportsAction)
            .compose(getAirportsUseCase)
            .subscribeOn(rxSchedulers.io)
            .subscribe { result ->
                when (result) {
                    is GetAirportsResult.Success -> initializationSubject.onNext(
                        InitializationUiState.Success(result.list)
                    )
                    is GetAirportsResult.Error -> initializationSubject.onNext(
                        InitializationUiState.Error(
                            result.throwable
                        )
                    )
                }
            }
    }

    private fun validateForm(validationContent: ValidationContent): List<Validation.ErrorType> {
        val mutableErrorList = mutableListOf<Validation.ErrorType>()
        if (validationContent.adults.toIntOrNull()?.let { number -> number < 0 } ?: true) mutableErrorList.add(Validation.ErrorType.ADULTS)
        if (validationContent.departureTime.isEmpty()) mutableErrorList.add(Validation.ErrorType.DEPARTURE)
        val originExpectedCode = getCode(validationContent.originStation)
        val destinationExpectedCode = getCode(validationContent.destinationStation)
        if (originExpectedCode == null || !stationRepository.isStationCodeCorrect(originExpectedCode)) mutableErrorList.add(
            Validation.ErrorType.ORIGIN_STATION
        )
        if (destinationExpectedCode == null || !stationRepository.isStationCodeCorrect(
                destinationExpectedCode
            )
        ) mutableErrorList.add(Validation.ErrorType.DESTINATION_STATION)
        return mutableErrorList.toList()
    }

    private fun mapToResult(errorList: List<Validation.ErrorType>): Validation {
        return if (errorList.isEmpty()) Validation.Success else Validation.Error(errorList)
    }

    private fun getCode(station: String): String? {
        return try {
            val length = station.length
            station.substring(length - 4, length - 1)
        } catch (error: IndexOutOfBoundsException) {
            null
        }
    }

    override fun onCleared() {
        clearDisposables.dispose()
        super.onCleared()
    }

    sealed class InitializationUiState {
        object Loading : InitializationUiState()
        data class Success(val list: List<StationAutoComplete>) : InitializationUiState()
        data class Error(val throwable: Throwable) : InitializationUiState()
    }

    data class ValidationContent(
        val originStation: String,
        val destinationStation: String,
        val departureTime: String,
        val adults: String
    )

    sealed class Validation {
        object Success : Validation()
        data class Error(val errorTypeList: List<ErrorType>) : Validation()

        enum class ErrorType {
            ORIGIN_STATION, DESTINATION_STATION, DEPARTURE, ADULTS
        }
    }
}