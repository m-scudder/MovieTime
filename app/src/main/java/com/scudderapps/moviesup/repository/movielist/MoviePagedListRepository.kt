package com.scudderapps.moviesup.repository.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.scudderapps.moviesup.api.POST_PER_PAGE
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.Movie
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: TheTMDBApiInterface) {

    lateinit var moviePageList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchingMovieList(
        compositeDisposable: CompositeDisposable,
        type: String
    ): LiveData<PagedList<Movie>> {

        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable, type)
        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePageList
    }

    fun getNerworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}