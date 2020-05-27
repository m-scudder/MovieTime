package com.scudderapps.moviesup.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.movie.MovieGenreListAdapter
import com.scudderapps.moviesup.adapter.tvshows.tvdetails.TvGenreListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.genre.GenreRepository
import com.scudderapps.moviesup.viewmodel.GenresViewModel


class DiscoverFragment : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.discover_movie_genre_list)
    lateinit var discoverMovieGenreList: RecyclerView

    @BindView(R.id.discover_tv_genre_list)
    lateinit var discoverTvGenreList: RecyclerView

    @BindView(R.id.discover_people_list)
    lateinit var discoverPeopleList: RecyclerView

    private lateinit var genresViewModel: GenresViewModel
    private lateinit var genreRepository: GenreRepository
    private lateinit var movieGenreListAdapter: MovieGenreListAdapter
    private lateinit var tvGenreListAdapter: TvGenreListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.discover_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        genreRepository = GenreRepository(apiService)
        genresViewModel = getGenreViewModel()
        populatingViews()
        return rootView
    }

    private fun populatingViews() {
        genresViewModel.movieGenresList.observe(viewLifecycleOwner, Observer {
            movieGenreListAdapter = MovieGenreListAdapter(it.genres, rootView.context)
            val linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            discoverMovieGenreList.layoutManager = linearLayoutManager
            discoverMovieGenreList.setHasFixedSize(true)
            discoverMovieGenreList.adapter = movieGenreListAdapter
        })

        genresViewModel.tvGenresList.observe(viewLifecycleOwner, Observer {
            tvGenreListAdapter = TvGenreListAdapter(it.genres, rootView.context)
            val linearLayoutManager2 = LinearLayoutManager(activity)
            linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
            discoverTvGenreList.layoutManager = linearLayoutManager2
            discoverTvGenreList.setHasFixedSize(true)
            discoverTvGenreList.adapter = tvGenreListAdapter
        })

    }

    private fun getGenreViewModel(): GenresViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GenresViewModel(genreRepository) as T
            }
        })[GenresViewModel::class.java]
    }

    override fun onResume() {
        populatingViews()
        super.onResume()
    }
}
