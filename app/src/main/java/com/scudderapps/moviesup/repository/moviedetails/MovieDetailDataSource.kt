package com.scudderapps.moviesup.repository.moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.MovieDetail

import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailDataSource(
    private val apiService: TheTMDBApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val netwrokState: LiveData<NetworkState>
        get() = _networkState


    private val _movieDetailsResponse = MutableLiveData<MovieDetail>()
    val movieDetailsResponse: LiveData<MovieDetail>
        get() = _movieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getMovieDetails(movieId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _movieDetailsResponse.postValue(it)
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