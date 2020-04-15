package com.scudderapps.moviesup.repository.genre

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.models.GenresResponse
import io.reactivex.disposables.CompositeDisposable

class GenreRepository(private val apiService: TheTMDBApiInterface) {

    lateinit var genresDataSource: GenreDataSource

    fun fetchingGenresResponse(
        compositeDisposable: CompositeDisposable
    ): LiveData<GenresResponse> {
        genresDataSource =
            GenreDataSource(
                apiService,
                compositeDisposable
            )
        genresDataSource.fetchGenresList()

        return genresDataSource.genresResponse
    }
}