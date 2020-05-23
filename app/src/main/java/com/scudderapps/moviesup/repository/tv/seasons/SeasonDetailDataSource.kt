package com.scudderapps.moviesup.repository.tv.seasons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.ApiInterface
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
                        Log.e("TvDetailDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("TvDetailDataSource", e.message)
        }
    }
}