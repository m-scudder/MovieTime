package com.scudderapps.moviesup.repository.peoplelist


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.People
import io.reactivex.disposables.CompositeDisposable


class PeopleDataSourceFactory(
    private val apiService: TheTMDBApiInterface,
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