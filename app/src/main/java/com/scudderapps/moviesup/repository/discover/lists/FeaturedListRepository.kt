package com.scudderapps.moviesup.repository.discover.lists


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.scudderapps.moviesup.api.ImdbApiInterface
import com.scudderapps.moviesup.models.tmdb.TOP250MOVIES
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class FeaturedListRepository(private val apiService: ImdbApiInterface) {

    lateinit var featuredDetailDataSource: FeaturedListDataSource

    fun fetchingTopMovies(
        compositeDisposable: CompositeDisposable
    ): LiveData<TOP250MOVIES> {
        featuredDetailDataSource =
            FeaturedListDataSource(
                apiService,
                compositeDisposable
            )
        featuredDetailDataSource.fetch250Movies()

        return featuredDetailDataSource.topMovieResponse
    }

    fun getTvSeasonDetailNetworkState(): LiveData<NetworkState> {
        return featuredDetailDataSource.networkState
    }
}