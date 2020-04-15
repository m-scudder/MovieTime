package com.scudderapps.moviesup.repository.genre

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.GenresResponse
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GenreDataSource(
    private val apiService: TheTMDBApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _genresResponse = MutableLiveData<GenresResponse>()
    val genresResponse: LiveData<GenresResponse>
        get() = _genresResponse

    fun fetchGenresList() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getGenresList()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _genresResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDetailDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("MovieDetailDataSource", e.message)
        }
    }
}