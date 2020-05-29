package com.scudderapps.moviesup.repository.discover.lists


import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.featuredlist.FeatureMovieLists
import com.scudderapps.moviesup.models.featuredlist.FeaturedTvLists
import com.scudderapps.moviesup.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class FeaturedListRepository(
    private val apiService: TmdbApiInterface
) {

    lateinit var featuredDetailDataSource: FeaturedListDataSource

    fun fetchingFeaturedMovies(
        compositeDisposable: CompositeDisposable,
        listId: Int
    ): LiveData<FeatureMovieLists> {
        featuredDetailDataSource =
            FeaturedListDataSource(
                apiService,
                compositeDisposable,
                listId
            )
        featuredDetailDataSource.fetchFeaturedMovies()

        return featuredDetailDataSource.featureMovieResponse
    }

    fun fetchingFeaturedTv(
        compositeDisposable: CompositeDisposable,
        listId: Int
    ): LiveData<FeaturedTvLists> {
        featuredDetailDataSource =
            FeaturedListDataSource(
                apiService,
                compositeDisposable,
                listId
            )
        featuredDetailDataSource.fetchFeaturedTv()

        return featuredDetailDataSource.featureTvResponse
    }

    fun getTvSeasonDetailNetworkState(): LiveData<NetworkState> {
        return featuredDetailDataSource.networkState
    }
}