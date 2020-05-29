package com.scudderapps.moviesup.repository.movie.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.scudderapps.moviesup.api.POST_PER_PAGE
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.main.Movie
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class TrendingPagedListRepository(private val apiService: TmdbApiInterface) {

    lateinit var moviePageList: LiveData<PagedList<Movie>>
    lateinit var trendingDataSourceFactory: TrendingDataSourceFactory

    fun fetchingTrendingMovieList(
        compositeDisposable: CompositeDisposable,
        type: String
    ): LiveData<PagedList<Movie>> {

        trendingDataSourceFactory = TrendingDataSourceFactory(apiService, compositeDisposable, type)
        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(trendingDataSourceFactory, config).build()
        return moviePageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TrendingDataSource, NetworkState>(
            trendingDataSourceFactory.discoverLiveDataSource, TrendingDataSource::networkState
        )
    }
}

