package com.scudderapps.moviesup.repository.discover

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.main.People
import io.reactivex.disposables.CompositeDisposable

class PeopleDataSourceFactory(
    private val apiService: ApiInterface,
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