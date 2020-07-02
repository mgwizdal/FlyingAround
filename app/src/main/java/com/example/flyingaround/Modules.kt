package com.example.flyingaround

import com.example.flyingaround.db.FlyingAroundDatabase
import com.example.flyingaround.details.viewmodel.DetailsDialogFragmentViewModel
import com.example.flyingaround.resultlist.model.db.FlightsRepository
import com.example.flyingaround.resultlist.model.network.GetFlightsService
import com.example.flyingaround.resultlist.model.usecase.GetFlightsUseCase
import com.example.flyingaround.resultlist.viewmodel.ResultListActivityViewModel
import com.example.flyingaround.search.model.db.StationRepository
import com.example.flyingaround.search.model.network.SearchService
import com.example.flyingaround.search.model.usecase.GetAirportsAction
import com.example.flyingaround.search.model.usecase.GetAirportsResult
import com.example.flyingaround.search.model.usecase.GetAirportsUseCase
import com.example.flyingaround.search.viewmodel.SearchActivityViewModel
import com.facebook.stetho.okhttp3.StethoInterceptor
import example.mobile.engie.com.capfiszki.utils.RxSchedulers
import io.reactivex.ObservableTransformer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { GetAirportsUseCase(get(), get()) as ObservableTransformer<GetAirportsAction, GetAirportsResult> }
    single { GetFlightsUseCase(get(), get()) }
    single { StationRepository(get()) }
    single { FlightsRepository(get()) }
    viewModel { SearchActivityViewModel(get(), get(), get()) }
    viewModel { ResultListActivityViewModel(get(), get(), get()) }
    viewModel { DetailsDialogFragmentViewModel(get(), get()) }
    single { RxSchedulers() }
}

val dbModule = module {
    single {
        FlyingAroundDatabase.getInstance(androidApplication())
    }

    single {
        get<FlyingAroundDatabase>().stationDao()
    }

    single {
        get<FlyingAroundDatabase>().flightsDao()
    }
}

val networkModule = module {
    single { provideRetrofit(get()) }
    single { provideSearchService(get()) }
    single { provideGetFlightsService(get()) }
    single { provideOkHttpClient(get()) }
    single { provideStethoInterceptor() }
}

fun provideSearchService(retrofit: Retrofit): SearchService {
    return retrofit.create(SearchService::class.java)
}
fun provideGetFlightsService(retrofit: Retrofit): GetFlightsService {
    return retrofit.create(GetFlightsService::class.java)
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://tripstest.ryanair.com/")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addNetworkInterceptor(interceptor).build()
}

fun provideStethoInterceptor(): Interceptor = StethoInterceptor()