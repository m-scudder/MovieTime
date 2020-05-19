package com.scudderapps.moviesup.repository.tv

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TvDetailsDataSource(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _tvDetailsResponse = MutableLiveData<TvDetail>()
    val tvDetailsResponse: LiveData<TvDetail>
        get() = _tvDetailsResponse

    fun fetchTvDetails(tvId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getTVDetails(tvId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _tvDetailsResponse.postValue(it)
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