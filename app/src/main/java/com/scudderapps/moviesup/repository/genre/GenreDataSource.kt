package com.scudderapps.moviesup.repository.genre

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.common.GenresResponse
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GenreDataSource(
    private val apiService: TmdbApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _genresResponse = MutableLiveData<GenresResponse>()
    val genresResponse: LiveData<GenresResponse>
        get() = _genresResponse

    fun fetchMovieGenresList() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getMovieGenresList()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _genresResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("GenreDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("GenreDataSource", e.message)
        }
    }

    fun fetchTvGenresList() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getTvGenresList()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _genresResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("GenreDataSource", it.message)
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            Log.e("GenreDataSource", e.message)
        }
    }

}