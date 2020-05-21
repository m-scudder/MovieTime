package com.scudderapps.moviesup.repository.movie.discovery

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.movie.Movie
import io.reactivex.disposables.CompositeDisposable


class DiscoverDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val id: Int
) : DataSource.Factory<Int, Movie>() {

    val discoverLiveDataSource = MutableLiveData<DiscoverDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val discoverDataSource =
            DiscoverDataSource(
                apiService,
                compositeDisposable,
                id
            )

        discoverLiveDataSource.postValue(discoverDataSource)
        return discoverDataSource
    }
}