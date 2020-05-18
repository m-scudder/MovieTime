package com.scudderapps.moviesup.repository.movie.peoplelist


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.MovieApiInterface
import com.scudderapps.moviesup.models.main.People
import io.reactivex.disposables.CompositeDisposable


class PeopleDataSourceFactory(
    private val apiService: MovieApiInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, People>() {

    val peopleLiveDataSource = MutableLiveData<PeopleDataSource>()

    override fun create(): DataSource<Int, People> {
        val peopleDataSource =
            PeopleDataSource(
                apiService,
                compositeDisposable
            )

        peopleLiveDataSource.postValue(peopleDataSource)
        return peopleDataSource
    }
}