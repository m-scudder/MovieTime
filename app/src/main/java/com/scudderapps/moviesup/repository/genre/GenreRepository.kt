package com.scudderapps.moviesup.repository.genre

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.models.common.GenresResponse
import io.reactivex.disposables.CompositeDisposable

class GenreRepository(private val apiService: TmdbApiInterface) {

    lateinit var genresDataSource: GenreDataSource

    fun fetchingMovieGenresResponse(
        compositeDisposable: CompositeDisposable
    ): LiveData<GenresResponse> {
        genresDataSource =
            GenreDataSource(
                apiService,
                compositeDisposable
            )
        genresDataSource.fetchMovieGenresList()

        return genresDataSource.genresResponse
    }

    fun fetchingTvGenresResponse(
        compositeDisposable: CompositeDisposable
    ): LiveData<GenresResponse> {
        genresDataSource =
            GenreDataSource(
                apiService,
                compositeDisposable
            )
        genresDataSource.fetchTvGenresList()

        return genresDataSource.genresResponse
    }


}