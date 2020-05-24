package com.scudderapps.moviesup.repository.discovergenres.movies

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.main.Movie
import io.reactivex.disposables.CompositeDisposable


class MovieDiscoverDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val id: Int
) : DataSource.Factory<Int, Movie>() {

    val discoverLiveDataSource = MutableLiveData<MovieDiscoverDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val discoverDataSource =
            MovieDiscoverDataSource(
                apiService,
                compositeDisposable,
                id
            )

        discoverLiveDataSource.postValue(discoverDataSource)
        return discoverDataSource
    }
}