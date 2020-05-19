package com.scudderapps.moviesup.repository.tv

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class TvDetailRepository(private val apiService: ApiInterface) {

    lateinit var tvDetailDataSource: TvDetailsDataSource

    fun fetchingTvDetails(
        compositeDisposable: CompositeDisposable,
        movieID: Int
    ): LiveData<TvDetail> {
        tvDetailDataSource =
            TvDetailsDataSource(
                apiService,
                compositeDisposable
            )
        tvDetailDataSource.fetchTvDetails(movieID)

        return tvDetailDataSource.tvDetailsResponse
    }

    fun getTvDetailNetworkState(): LiveData<NetworkState> {
        return tvDetailDataSource.networkState
    }
}