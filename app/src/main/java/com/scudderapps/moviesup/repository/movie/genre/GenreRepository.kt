package com.scudderapps.moviesup.repository.movie.genre

import androidx.lifecycle.LiveData
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.models.common.GenresResponse
import io.reactivex.disposables.CompositeDisposable

class GenreRepository(private val apiService: ApiInterface) {

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