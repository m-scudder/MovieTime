package com.scudderapps.moviesup.repository.movie.movielist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.main.Movie
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(
    private val apiService: TmdbApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource =
            MovieDataSource(
                apiService,
                compositeDisposable,
                type
            )

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}