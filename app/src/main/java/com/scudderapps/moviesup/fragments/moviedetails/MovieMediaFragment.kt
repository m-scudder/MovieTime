package com.scudderapps.moviesup.fragments.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.movie.moviedetails.TrailerListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.movie.Backdrop
import com.scudderapps.moviesup.models.movie.Poster
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel
import com.stfalcon.imageviewer.StfalconImageViewer

class MovieMediaFragment(private var movieId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.trailerListView)
    lateinit var trailerListView: RecyclerView

    @BindView(R.id.trailer_layout)
    lateinit var trailerLayout: LinearLayout

    @BindView(R.id.posterMedia)
    lateinit var posterMedia: ImageView

    @BindView(R.id.backdropMedia)
    lateinit var backdropMedia: ImageView

    @BindView(R.id.posterCount)
    lateinit var poster_count: TextView

    @BindView(R.id.backdropCount)
    lateinit var backdrop_count: TextView

    @BindView(R.id.posterlayout)
    lateinit var posterLayout: LinearLayout

    @BindView(R.id.backdropLayout)
    lateinit var backdropLayout: LinearLayout

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    private lateinit var trailerAdapter: TrailerListAdapter
    private lateinit var movieBackdrops: List<Backdrop>
    private lateinit var moviePosters: List<Poster>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.movie_media_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)
        populatingViews()
        return rootView
    }

    private fun populatingViews() {
        viewModel.videoDetails.observe(this, Observer {

            if (!it.videosList.isNullOrEmpty()) {
                trailerAdapter = TrailerListAdapter(it.videosList, rootView.context)
                val linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                trailerListView.layoutManager = linearLayoutManager
                trailerListView.setHasFixedSize(true)
                trailerListView.adapter = trailerAdapter
            } else {
                trailerLayout.visibility = View.GONE
            }

        })

        viewModel.movieMedia.observe(this, Observer {
            movieBackdrops = it.backdrops
            moviePosters = it.posters
            if (!moviePosters.isNullOrEmpty()) {
                var mediaPosterURL = moviePosters[0].filePath
                val mediaPosterUrl: String = IMAGE_BASE_URL + mediaPosterURL
                Glide.with(this).load(mediaPosterUrl).into(posterMedia)
                if (moviePosters.size == 1) {
                    poster_count.text = moviePosters.size.toString() + " Poster"
                } else {
                    poster_count.text = moviePosters.size.toString() + " Posters"
                }
                posterMedia.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        moviePosters
                    ) { posterMedia: ImageView, poster: Poster ->
                        Glide.with(this).load(IMAGE_BASE_URL + poster.filePath).into(posterMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside().into(posterMedia)
                poster_count.text = "No Posters"
            }
            if (!movieBackdrops.isNullOrEmpty()) {
                var mediaBackdropURL = movieBackdrops[0].filePath
                val mediaBackdropUrl: String = IMAGE_BASE_URL + mediaBackdropURL
                Glide.with(this).load(mediaBackdropUrl).into(backdropMedia)
                if (movieBackdrops.size == 1) {
                    backdrop_count.text = movieBackdrops.size.toString() + " Backdrop"
                } else {
                    backdrop_count.text = movieBackdrops.size.toString() + " Backdrops"
                }

                backdropMedia.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        movieBackdrops
                    ) { backdropMedia: ImageView, backdrop: Backdrop ->
                        Glide.with(this).load(IMAGE_BASE_URL + backdrop.filePath)
                            .into(backdropMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside().into(backdropMedia)
                backdrop_count.text = "No Backdrops"
            }
        })

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
