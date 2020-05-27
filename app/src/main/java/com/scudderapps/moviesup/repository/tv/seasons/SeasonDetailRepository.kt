package com.scudderapps.moviesup.repository.tv.seasons

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.common.CastResponse
import com.scudderapps.moviesup.models.common.VideoResponse
import com.scudderapps.moviesup.models.tv.TvSeasonDetails
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class SeasonDetailRepository(private val apiService: TmdbApiInterface) {

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

    fun fetchingTvSeasonVideos(
        compositeDisposable: CompositeDisposable,
        tvId: Int,
        seasonNumber: Int
    ): LiveData<VideoResponse> {
        tvSeasonDetailDataSource =
            SeasonDetailDataSource(
                apiService,
                compositeDisposable
            )
        tvSeasonDetailDataSource.fetchTvSeasonVideos(tvId, seasonNumber)

        return tvSeasonDetailDataSource.tvSeasonVideoResponse
    }

    fun fetchingTvSeasonCast(
        compositeDisposable: CompositeDisposable,
        tvId: Int,
        seasonNumber: Int
    ): LiveData<CastResponse> {
        tvSeasonDetailDataSource =
            SeasonDetailDataSource(
                apiService,
                compositeDisposable
            )
        tvSeasonDetailDataSource.fetchTvSeasonCast(tvId, seasonNumber)

        return tvSeasonDetailDataSource.tvSeasonCastResponse
    }

    fun getTvSeasonDetailNetworkState(): LiveData<NetworkState> {
        return tvSeasonDetailDataSource.networkState
    }
}