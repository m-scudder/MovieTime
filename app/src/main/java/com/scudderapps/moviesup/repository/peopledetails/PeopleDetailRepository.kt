package com.scudderapps.moviesup.repository.peopledetails

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.MovieCredits
import com.scudderapps.moviesup.models.PeopleDetails
import io.reactivex.disposables.CompositeDisposable

class PeopleDetailRepository(private val apiService: TheTMDBApiInterface) {

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

}