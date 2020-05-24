package com.scudderapps.moviesup.repository.tv.seasons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.common.CastResponse
import com.scudderapps.moviesup.models.common.VideoResponse
import com.scudderapps.moviesup.models.tv.TvSeasonDetails
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SeasonDetailDataSource(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _tvSeasonDetailsResponse = MutableLiveData<TvSeasonDetails>()
    val tvSeasonDetailsResponse: LiveData<TvSeasonDetails>
        get() = _tvSeasonDetailsResponse

    private val _tvSeasonVideoResponse = MutableLiveData<VideoResponse>()
    val tvSeasonVideoResponse: LiveData<VideoResponse>
        get() = _tvSeasonVideoResponse

    private val _tvSeasonCastResponse = MutableLiveData<CastResponse>()
    val tvSeasonCastResponse: LiveData<CastResponse>
        get() = _tvSeasonCastResponse

    fun fetchTvSeasonDetails(tvId: Int, seasonNumber: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getTvSeason(tvId, seasonNumber)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _tvSeasonDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("SeasonDetailDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("SeasonDetailDataSource", e.message)
        }
    }

    fun fetchTvSeasonVideos(tvId: Int, seasonNumber: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getTvSeasonVideos(tvId, seasonNumber)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _tvSeasonVideoResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("SeasonDetailDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("SeasonDetailDataSource", e.message)
        }
    }

    fun fetchTvSeasonCast(tvId: Int, seasonNumber: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getTvSeasonCast(tvId, seasonNumber)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _tvSeasonCastResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("SeasonDetailDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("SeasonDetailDataSource", e.message)
        }
    }
}