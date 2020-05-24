package com.scudderapps.moviesup.repository.discovergenres.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.scudderapps.moviesup.api.POST_PER_PAGE
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.main.Movie
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDiscoverPagedListRepository(private val apiService: ApiInterface) {

    lateinit var moviePageList: LiveData<PagedList<Movie>>
    lateinit var discoverDataSourceFactory: MovieDiscoverDataSourceFactory

    fun fetchingMovieList(
        compositeDisposable: CompositeDisposable,
        id: Int
    ): LiveData<PagedList<Movie>> {

        discoverDataSourceFactory = MovieDiscoverDataSourceFactory(apiService, compositeDisposable, id)
        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(discoverDataSourceFactory, config).build()
        return moviePageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDiscoverDataSource, NetworkState>(
            discoverDataSourceFactory.discoverLiveDataSource, MovieDiscoverDataSource::networkState
        )
    }
}