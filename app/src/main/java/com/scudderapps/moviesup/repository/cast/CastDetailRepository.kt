package com.scudderapps.moviesup.repository.cast

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.common.MovieCredits
import com.scudderapps.moviesup.models.common.TvCredits
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.models.main.PeopleImages
import io.reactivex.disposables.CompositeDisposable

class CastDetailRepository(private val apiService: ApiInterface) {

    lateinit var peopleDetailDataSource: CastDetailDataSource

    fun fetchingPeoplesDetails(
        compositeDisposable: CompositeDisposable,
        peopleID: Int
    ): LiveData<PeopleDetails> {
        peopleDetailDataSource =
            CastDetailDataSource(
                apiService,
                compositeDisposable
            )
        peopleDetailDataSource.fetchPeopleDetails(peopleID)

        return peopleDetailDataSource.peopleDetailsResponse
    }

    fun fetchingMovieCredits(
        compositeDisposable: CompositeDisposable,
        peopleID: Int
    ): LiveData<MovieCredits> {
        peopleDetailDataSource =
            CastDetailDataSource(
                apiService,
                compositeDisposable
            )
        peopleDetailDataSource.fetchMovieCredits(peopleID)

        return peopleDetailDataSource.movieCreditsResponse
    }

    fun fetchingTvCredits(
        compositeDisposable: CompositeDisposable,
        peopleID: Int
    ): LiveData<TvCredits> {
        peopleDetailDataSource =
            CastDetailDataSource(
                apiService,
                compositeDisposable
            )
        peopleDetailDataSource.fetchTvCredits(peopleID)

        return peopleDetailDataSource.tvCreditsResponse
    }

    fun fetchingPeopleImages(
        compositeDisposable: CompositeDisposable,
        peopleID: Int
    ): LiveData<PeopleImages> {
        peopleDetailDataSource =
            CastDetailDataSource(
                apiService,
                compositeDisposable
            )
        peopleDetailDataSource.fetchPeopleImages(peopleID)

        return peopleDetailDataSource.peopleProfileImages
    }

}