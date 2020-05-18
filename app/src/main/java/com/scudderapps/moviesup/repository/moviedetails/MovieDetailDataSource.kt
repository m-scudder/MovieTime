package com.scudderapps.moviesup.repository.moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.movie.*
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailDataSource(
    private val apiService: TheTMDBApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
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

    private val _movieMediaResponse = MutableLiveData<MediaResponse>()
    val movieMediaResponse: LiveData<MediaResponse>
        get() = _movieMediaResponse

    private val _collectionResponse = MutableLiveData<CollectionResponse>()
    val collectionResponse: LiveData<CollectionResponse>
        get() = _collectionResponse

    fun fetchCollections(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getCollections(movieId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _collectionResponse.postValue(it)
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

    fun fetchMoviesMedia(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiService.getMovieMedia(movieId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _movieMediaResponse.postValue(it)
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