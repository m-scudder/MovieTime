package com.scudderapps.moviesup

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.adapter.movie.MovieDetailTabAdapter
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.movie.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailActivity : AppCompatActivity() {

    @BindView(R.id.backdrop_image)
    lateinit var backdropImage: ImageView

    @BindView(R.id.poster_image)
    lateinit var posterImage: ImageView

    @BindView(R.id.movie_title)
    lateinit var title: TextView

    @BindView(R.id.releaseDate)
    lateinit var releaseDate: TextView

    @BindView(R.id.runTime)
    lateinit var runTime: TextView

    @BindView(R.id.status)
    lateinit var status: TextView

    @BindView(R.id.detail_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.movie_detail_tab_layout)
    lateinit var movieDetailTabLayout: TabLayout

    @BindView(R.id.movie_detail_viewpager)
    lateinit var movieDetailViewPager: ViewPager

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
            supportActionBar!!.title = ""
        val data = intent.extras
        var movieId = data!!.getInt("id")

        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)

        movieDetailTabLayout.addTab(movieDetailTabLayout.newTab().setText("About"))
        movieDetailTabLayout.addTab(movieDetailTabLayout.newTab().setText("Cast"))
        movieDetailTabLayout.addTab(movieDetailTabLayout.newTab().setText("Crew"))

        val adapter =
            MovieDetailTabAdapter(
                movieId,
                this,
                supportFragmentManager,
                movieDetailTabLayout.tabCount
            )
        movieDetailViewPager.adapter = adapter

        movieDetailTabLayout.setupWithViewPager(movieDetailViewPager)
        movieDetailViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                movieDetailTabLayout
            )
        )

        viewModel.movieDetails.observe(this, Observer {
            title.text = it.title
//            supportActionBar!!.title = it.title
            if (!it.releaseDate.isNullOrEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(it.releaseDate)
                val formattedDate: String = targetFormat.format(date)
                releaseDate.text = formattedDate + "  ●"
            }
            runTime.text = "${it.runtime} Min"
            status.text = it.status
            if (!it.backdropPath.isNullOrEmpty()) {
                var backDropURL: String = IMAGE_BASE_URL + it.backdropPath
                Glide.with(this)
                    .load(backDropURL)
                    .into(backdropImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.no_image_found)
                    .centerInside()
                    .into(backdropImage)
            }

            if (!it.posterPath.isNullOrEmpty()) {
                val moviePosterURL: String = IMAGE_BASE_URL + it.posterPath
                Glide.with(this)
                    .load(moviePosterURL)
                    .into(posterImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.no_image_found)
                    .centerInside()
                    .into(posterImage)
            }
        })

//        viewModel.networkState.observe(this, Observer {
//            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
//        })
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