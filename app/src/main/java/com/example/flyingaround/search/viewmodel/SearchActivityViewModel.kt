package com.example.flyingaround.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.flyingaround.search.model.db.StationRepository
import com.example.flyingaround.search.model.usecase.GetAirportsAction
import com.example.flyingaround.search.model.usecase.GetAirportsResult
import com.example.flyingaround.search.model.usecase.GetAirportsUseCase
import com.example.flyingaround.search.view.StationAutoComplete
import com.example.flyingaround.utils.include
import example.mobile.engie.com.capfiszki.utils.RxSchedulers
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class SearchActivityViewModel(
    private val getAirportsUseCase: ObservableTransformer<GetAirportsAction, GetAirportsResult>,
    private val stationRepository: StationRepository,
    private val rxSchedulers: RxSchedulers
) : ViewModel() {
    private val clearDisposables = CompositeDisposable()
    private val initializationSubject = PublishSubject.create<InitializationUiState>()
    val initializationObservable: Observable<InitializationUiState> = initializationSubject
        .startWith(InitializationUiState.Loading)
        .observeOn(rxSchedulers.ui)

    private val validationSubject = PublishSubject.create<ValidationContent>()
    val validationObservable: Observable<Validation> = validationSubject
        .subscribeOn(rxSchedulers.disk)
        .observeOn(rxSchedulers.disk)
        .map(::validateForm)
        .observeOn(rxSchedulers.ui)

    fun search(
        originStation: String,
        destination: String,
        departureTime: String,
        adults: String,
        teens: String?,
        children: String?
    ) {
        validationSubject.onNext(
            ValidationContent(
                originStation,
                destination,
                departureTime,
                adults,
                teens,
                children
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
                    is GetAirportsResult.Error -> {
                        Log.e("${this@SearchActivityViewModel.javaClass}", result.throwable.localizedMessage ?: "GetAirportsResultError")
                        initializationSubject.onNext(
                            InitializationUiState.Error(
                                result.throwable
                            )
                        )
                    }
                }
            }
    }

    private fun validateForm(validationContent: ValidationContent): Validation {
        val mutableErrorList = mutableListOf<Validation.ErrorType>()

        val originExpectedCode = getCode(validationContent.originStation)
        val destinationExpectedCode = getCode(validationContent.destinationStation)
        if (originExpectedCode == null || !stationRepository.isStationCodeCorrect(originExpectedCode)) mutableErrorList.add(
            Validation.ErrorType.ORIGIN_STATION
        )
        if (destinationExpectedCode == null || !stationRepository.isStationCodeCorrect(
                destinationExpectedCode
            )
        ) mutableErrorList.add(Validation.ErrorType.DESTINATION_STATION)

        if (validationContent.adults.toIntOrNull()
                ?.let { number -> number < 0 } != false
        ) mutableErrorList.add(Validation.ErrorType.ADULTS)
        if (validationContent.departureTime.isEmpty()) mutableErrorList.add(Validation.ErrorType.DEPARTURE)

        return if (mutableErrorList.isEmpty()) {
            Validation.Success(
                requireNotNull(originExpectedCode),
                requireNotNull(destinationExpectedCode),
                validationContent.departureTime,
                requireNotNull(validationContent.adults.toIntOrNull()),
                validationContent.teens?.toIntOrNull() ?: 0,
                validationContent.children?.toIntOrNull() ?: 0
            )
        } else {
            Validation.Error(mutableErrorList.toList())
        }
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
        val adults: String,
        val teens: String? = null,
        val children: String? = null
    )

    sealed class Validation {
        data class Success(
            val originStation: String,
            val destinationStation: String,
            val departureTime: String,
            val adults: Int,
            val teens: Int = 0,
            val children: Int = 0
        ) : Validation()

        data class Error(val errorTypeList: List<ErrorType>) : Validation()

        enum class ErrorType {
            ORIGIN_STATION, DESTINATION_STATION, DEPARTURE, ADULTS
        }
    }
}