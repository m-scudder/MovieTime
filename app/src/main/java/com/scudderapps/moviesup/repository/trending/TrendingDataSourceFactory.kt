package com.scudderapps.moviesup.repository.trending

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.Movie
import io.reactivex.disposables.CompositeDisposable

class TrendingDataSourceFactory(
    private val apiService: TheTMDBApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
) : DataSource.Factory<Int, Movie>() {

    val discoverLiveDataSource = MutableLiveData<TrendingDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val discoverDataSource =
            TrendingDataSource(
                apiService,
                compositeDisposable,
                type
            )

        discoverLiveDataSource.postValue(discoverDataSource)
        return discoverDataSource
    }
}