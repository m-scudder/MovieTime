package com.scudderapps.moviesup.repository.movie.peopledetails

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.movie.MovieCredits
import com.scudderapps.moviesup.models.main.PeopleDetails
import com.scudderapps.moviesup.models.main.PeopleImages
import io.reactivex.disposables.CompositeDisposable

class PeopleDetailRepository(private val apiService: ApiInterface) {

    lateinit var peopleDetailDataSource: PeopleDetailDataSource

    fun fetchingPeoplesDetails(
        compositeDisposable: CompositeDisposable,
        peopleID: Int
    ): LiveData<PeopleDetails> {
        peopleDetailDataSource =
            PeopleDetailDataSource(
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
            PeopleDetailDataSource(
                apiService,
                compositeDisposable
            )
        peopleDetailDataSource.fetchMovieCredits(peopleID)

        return peopleDetailDataSource.movieCreditsResponse
    }

    fun fetchingPeopleImages(
        compositeDisposable: CompositeDisposable,
        peopleID: Int
    ): LiveData<PeopleImages> {
        peopleDetailDataSource =
            PeopleDetailDataSource(
                apiService,
                compositeDisposable
            )
        peopleDetailDataSource.fetchPeopleImages(peopleID)

        return peopleDetailDataSource.peopleProfileImages
    }

}