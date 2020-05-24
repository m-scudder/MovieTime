package com.scudderapps.moviesup.repository.tv.tvdetails

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.common.CastResponse
import com.scudderapps.moviesup.models.common.MediaResponse
import com.scudderapps.moviesup.models.common.VideoResponse
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class TvDetailRepository(private val apiService: ApiInterface) {

    lateinit var tvDetailDataSource: TvDetailsDataSource

    fun fetchingTvDetails(
        compositeDisposable: CompositeDisposable,
        tvId: Int
    ): LiveData<TvDetail> {
        tvDetailDataSource =
            TvDetailsDataSource(
                apiService,
                compositeDisposable
            )
        tvDetailDataSource.fetchTvDetails(tvId)

        return tvDetailDataSource.tvDetailsResponse
    }

    fun fetchingTvVideos(
        compositeDisposable: CompositeDisposable,
        tvId: Int
    ): LiveData<VideoResponse> {
        tvDetailDataSource =
            TvDetailsDataSource(
                apiService,
                compositeDisposable
            )
        tvDetailDataSource.fetchTvVideos(tvId)

        return tvDetailDataSource.tvVideosResponse
    }

    fun fetchingTvMedia(
        compositeDisposable: CompositeDisposable,
        tvId: Int
    ): LiveData<MediaResponse> {
        tvDetailDataSource =
            TvDetailsDataSource(
                apiService,
                compositeDisposable
            )
        tvDetailDataSource.fetchMoviesMedia(tvId)

        return tvDetailDataSource.tvMediaResponse
    }

    fun fetchingTvCastDetails(
        compositeDisposable: CompositeDisposable,
        tvId: Int
    ): LiveData<CastResponse> {
        tvDetailDataSource =
            TvDetailsDataSource(
                apiService,
                compositeDisposable
            )
        tvDetailDataSource.fetchTvCastDetails(tvId)

        return tvDetailDataSource.tvCastResponse
    }

    fun getTvDetailNetworkState(): LiveData<NetworkState> {
        return tvDetailDataSource.networkState
    }
}