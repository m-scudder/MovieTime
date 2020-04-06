package com.scudderapps.moviesup.repository.moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.CastResponse
import com.scudderapps.moviesup.models.MovieDetail
import com.scudderapps.moviesup.models.VideoResponse
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

    private val _movieVideoResponse = MutableLiveData<VideoResponse>()
    val movieVideoResponse: LiveData<VideoResponse>
        get() = _movieVideoResponse

    private val _movieCastResponse = MutableLiveData<CastResponse>()
    val movieCastResponse: LiveData<CastResponse>
        get() = _movieCastResponse

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

    fun fetchMovieVideos(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getMovieVideos(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _movieVideoResponse.postValue(it)
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

    fun fetchCastDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getMovieCast(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _movieCastResponse.postValue(it)
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