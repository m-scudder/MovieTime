package com.scudderapps.moviesup.repository.discovergenres.tvshows


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.main.Movie
import com.scudderapps.moviesup.models.main.TV
import io.reactivex.disposables.CompositeDisposable


class TvDiscoverDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val id: Int
) : DataSource.Factory<Int, TV>() {

    val discoverTvLiveDataSource = MutableLiveData<TvDiscoverDataSource>()

    override fun create(): DataSource<Int, TV> {
        val discoverDataSource =
            TvDiscoverDataSource(
                apiService,
                compositeDisposable,
                id
            )

        discoverTvLiveDataSource.postValue(discoverDataSource)
        return discoverDataSource
    }
}