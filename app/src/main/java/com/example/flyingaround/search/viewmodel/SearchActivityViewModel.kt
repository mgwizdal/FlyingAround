package com.example.flyingaround.search.viewmodel

import androidx.lifecycle.ViewModel
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
    private val rxSchedulers: RxSchedulers
) : ViewModel() {
    private val clearDisposables = CompositeDisposable()
    private val initializationSubject = PublishSubject.create<InitializationUiState>()
    val initializationObservable: Observable<InitializationUiState> = initializationSubject
        .startWith(InitializationUiState.Loading)
        .observeOn(rxSchedulers.ui)
    
    fun initialize() {
        clearDisposables include Observable.just(GetAirportsAction)
            .compose(getAirportsUseCase)
            .subscribeOn(rxSchedulers.io)
            .subscribe { result ->
                when (result) {
                    is GetAirportsResult.Success -> initializationSubject.onNext(InitializationUiState.Success(result.list))
                    is GetAirportsResult.Error -> initializationSubject.onNext(
                        InitializationUiState.Error(
                            result.throwable
                        )
                    )
                }
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
}