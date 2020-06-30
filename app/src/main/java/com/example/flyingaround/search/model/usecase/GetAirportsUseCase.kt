package com.example.flyingaround.search.model.usecase

import com.example.flyingaround.search.model.db.StationDao
import com.example.flyingaround.search.network.SearchService
import com.example.flyingaround.utils.toBodyOrError
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class GetAirportsUseCase(
    private val searchService: SearchService,
    private val stationDao: StationDao
): ObservableTransformer<GetAirportsAction, GetAirportsResult> {
    override fun apply(upstream: Observable<GetAirportsAction>): ObservableSource<GetAirportsResult> {
        return upstream.switchMap {
                searchService.getAirports().toObservable()
                    .map { it.toBodyOrError() }
                    .map { container -> container.stations.map { dto -> dto.mapToEntity() } }
                    .map { stationDao.insertStations(*it.toTypedArray()) }
                    .map{ GetAirportsResult.Success as GetAirportsResult}
                    .onErrorReturn{ GetAirportsResult.Error(it) }
            }
    }

}

object GetAirportsAction
sealed class GetAirportsResult {
    object Success: GetAirportsResult()
    data class Error(val throwable: Throwable): GetAirportsResult()
}
