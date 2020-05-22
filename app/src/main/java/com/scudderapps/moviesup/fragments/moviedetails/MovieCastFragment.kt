package com.scudderapps.moviesup.fragments.moviedetails

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
import com.scudderapps.moviesup.adapter.movie.moviedetails.CastListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.common.CastDetail
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel

class MovieCastFragment(private var movieId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.movie_cast_list)
    lateinit var castListView: RecyclerView

    private lateinit var castAdapter: CastListAdapter
    private lateinit var castDetail: ArrayList<CastDetail>

    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.movie_cast_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.castDetails.observe(viewLifecycleOwner, Observer {
            castDetail = it.castDetail
            castAdapter = CastListAdapter(castDetail, rootView.context)
            linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            castListView.layoutManager = linearLayoutManager
            castListView.setHasFixedSize(true)
            castListView.adapter = castAdapter

        })
        return rootView
    }

    private fun getViewModel(movieId: Int): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}
