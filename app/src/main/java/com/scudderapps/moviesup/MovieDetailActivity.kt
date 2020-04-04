package com.scudderapps.moviesup

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.api.POSTER_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.Genre
import com.scudderapps.moviesup.models.MovieDetail
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel

class MovieDetailActivity : AppCompatActivity() {

    @BindView(R.id.backdrop_image)
    lateinit var backdropImage: ImageView

    @BindView(R.id.poster_image)
    lateinit var posterImage: ImageView

    @BindView(R.id.movie_overview)
    lateinit var movieOverview: TextView

    @BindView(R.id.movie_title)
    lateinit var title: TextView

    @BindView(R.id.movie_release_date)
    lateinit var movieReleaseDate: TextView

    @BindView(R.id.imdb_rating)
    lateinit var imdbRating: TextView

    @BindView(R.id.likes_count)
    lateinit var likeCount: TextView

    @BindView(R.id.genresName)
    lateinit var genresName: TextView

    @BindView(R.id.runTime)
    lateinit var runTime: TextView

    @BindView(R.id.status)
    lateinit var movieStatus: TextView

    @BindView(R.id.detail_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.detailProgressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.text_error_movie_detail)
    lateinit var errorTextView: TextView

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("")

        val data = intent.extras
        var movieId = data!!.getInt("id")

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        movieRepository =
            MovieDetailRepository(
                apiService
            )
        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
//            relativeLayout.visibility = if (it == NetwrokState.LOADED) View.VISIBLE else View.GONE
            errorTextView.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it: MovieDetail) {
        title.text = it.title
        movieOverview.text = it.overview
        imdbRating.text = "\uD83C\uDF1F " + it.voteAverage.toString()
        likeCount.text = "\uD83D\uDC4D " + it.voteCount
        movieReleaseDate.text = "\uD83D\uDCC5 " + it.releaseDate
        runTime.text = "Runtime: " + it.runtime.toString() + " Min"
        movieStatus.text = "Status: " + it.status

        val genre: ArrayList<Genre> = it.genres
        for (i in genre) {
            genresName.append("\u25CF ${i.name}  ")
        }

        val moviePosterURL: String = POSTER_BASE_URL + it.posterPath
        val backDropURL: String = POSTER_BASE_URL + it.backdropPath

        Glide.with(this)
            .load(backDropURL)
            .placeholder(R.drawable.no_image_found)
            .into(backdropImage)

        Glide.with(this)
            .load(moviePosterURL)
            .placeholder(R.drawable.no_image_found)
            .into(posterImage)


    }

    private fun getViewModel(movieId: Int): MovieDetailViewModel {
//        return ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}