package com.example.flyingaround.search.viewmodel

import com.example.flyingaround.search.model.db.StationRepository
import com.example.flyingaround.search.model.usecase.GetAirportsAction
import com.example.flyingaround.search.model.usecase.GetAirportsResult
import com.example.flyingaround.search.view.StationAutoComplete
import example.mobile.engie.com.capfiszki.utils.RxSchedulers
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchActivityViewModelTest {

    private val useCase = MockGetAirportsUseCase()

    @Mock
    private lateinit var stationRepository: StationRepository

    private val schedulers = RxSchedulers(
        Schedulers.trampoline(),
        Schedulers.trampoline(),
        Schedulers.trampoline(),
        Schedulers.trampoline()
    )

    private lateinit var viewModel: SearchActivityViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchActivityViewModel(useCase, stationRepository, schedulers)
    }

    @Test
    fun `Sample test - initialiation`() {
        //given
        val observer = viewModel.initializationObservable.test()

        //when
        viewModel.initialize()

        //then
        observer.assertValues(
            SearchActivityViewModel.InitializationUiState.Loading,
            SearchActivityViewModel.InitializationUiState.Success(MockGetAirportsUseCase.LIST)
        )
    }
}

class MockGetAirportsUseCase : ObservableTransformer<GetAirportsAction, GetAirportsResult> {
    override fun apply(upstream: Observable<GetAirportsAction>): ObservableSource<GetAirportsResult> {
        return upstream.map { GetAirportsResult.Success(LIST) }
    }

    companion object {
        val LIST = listOf(
            StationAutoComplete("WRO", "Wroclaw", "Breslau")
        )
    }
}