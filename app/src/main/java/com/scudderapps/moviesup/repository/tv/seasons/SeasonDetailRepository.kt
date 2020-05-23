package com.scudderapps.moviesup.repository.tv.seasons

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.tv.TvSeasonDetails
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class SeasonDetailRepository(private val apiService: ApiInterface) {

    lateinit var tvSeasonDetailDataSource: SeasonDetailDataSource

    fun fetchingTvSeasonDetails(
        compositeDisposable: CompositeDisposable,
        tvId: Int,
        seasonNumber: Int
    ): LiveData<TvSeasonDetails> {
        tvSeasonDetailDataSource =
            SeasonDetailDataSource(
                apiService,
                compositeDisposable
            )
        tvSeasonDetailDataSource.fetchTvSeasonDetails(tvId, seasonNumber)

        return tvSeasonDetailDataSource.tvSeasonDetailsResponse
    }

    fun getTvSeasonDetailNetworkState(): LiveData<NetworkState> {
        return tvSeasonDetailDataSource.networkState
    }
}