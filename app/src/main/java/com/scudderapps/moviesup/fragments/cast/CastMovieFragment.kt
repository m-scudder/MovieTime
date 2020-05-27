package com.scudderapps.moviesup.fragments.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.castandncrew.MovieAdapter
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.cast.CastDetailRepository
import com.scudderapps.moviesup.viewmodel.CastDetailViewModel

class CastMovieFragment(val castId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.movie_credits_list)
    lateinit var castMovieList: RecyclerView

    @BindView(R.id.cast_no_image_found)
    lateinit var noImageFound: ImageView

    private lateinit var movieAdapter: MovieAdapter
    lateinit var peopleDetailRepository: CastDetailRepository
    private lateinit var peopleDetailViewModel: CastDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.cast_movie_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        peopleDetailRepository =
            CastDetailRepository(
                apiService
            )
        peopleDetailViewModel = getViewModel(castId)

        peopleDetailViewModel.movieCredits.observe(viewLifecycleOwner, Observer {

            if (!it.cast.isNullOrEmpty()) {
                movieAdapter =
                    MovieAdapter(
                        it.cast,
                        rootView.context
                    )
                val layoutManager = GridLayoutManager(activity, 3)
                castMovieList.layoutManager = layoutManager
                castMovieList.setHasFixedSize(true)
                castMovieList.adapter = movieAdapter
            } else {
                noImageFound.visibility = View.VISIBLE
            }
        })


        return rootView
    }

    private fun getViewModel(peopleID: Int): CastDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CastDetailViewModel(peopleDetailRepository, peopleID) as T
            }
        })[CastDetailViewModel::class.java]
    }
}
