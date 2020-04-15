package com.scudderapps.moviesup

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.adapter.CastListAdapter
import com.scudderapps.moviesup.adapter.TrailerListAdapter
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.*
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.moviedetails.MovieDetailRepository
import com.scudderapps.moviesup.viewmodel.MovieDetailViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MovieDetailActivity : AppCompatActivity() {

    @BindView(R.id.backdrop_image)
    lateinit var backdropImage: ImageView

    @BindView(R.id.poster_image)
    lateinit var posterImage: ImageView

    @BindView(R.id.movie_overview)
    lateinit var movieOverview: ExpandableTextView

    @BindView(R.id.movie_title)
    lateinit var title: TextView

    @BindView(R.id.releaseDate)
    lateinit var releaseDate: TextView

    @BindView(R.id.runTime)
    lateinit var runTime: TextView

    @BindView(R.id.status)
    lateinit var status: TextView

    @BindView(R.id.genresName)
    lateinit var genresName: TextView

    @BindView(R.id.detail_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.movieDetailBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.trailerListView)
    lateinit var trailerList: RecyclerView

    @BindView(R.id.castListView)
    lateinit var castList: RecyclerView

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

    @BindView(R.id.trailer_layout)
    lateinit var trailerLayout: LinearLayout

    @BindView(R.id.synopsis_layout)
    lateinit var synopsisLayout: LinearLayout

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    private lateinit var trailerAdapter: TrailerListAdapter
    private lateinit var trailerDetail: ArrayList<VideoDetail>

    private lateinit var castAdapter: CastListAdapter
    private lateinit var castDetail: ArrayList<CastDetail>
    private lateinit var movieBackdrops: List<Backdrop>
    private lateinit var moviePosters: List<Poster>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("")

        val data = intent.extras
        var movieId = data!!.getInt("id")

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val linearLayoutManager2 = LinearLayoutManager(this)
        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        movieRepository = MovieDetailRepository(apiService)
        viewModel = getViewModel(movieId)

        viewModel.videoDetails.observe(this, Observer {

            if (!it.videosList.isNullOrEmpty()) {
                trailerAdapter = TrailerListAdapter(it.videosList, this)
                trailerList.layoutManager = linearLayoutManager
                trailerList.setHasFixedSize(true)
                trailerList.adapter = trailerAdapter
            } else {
                trailerLayout.visibility = View.GONE
            }

        })

        viewModel.castDetails.observe(this, Observer {
            castDetail = it.castDetail
            castAdapter = CastListAdapter(castDetail, this)
            castList.layoutManager = linearLayoutManager2
            castList.setHasFixedSize(true)
            castList.adapter = castAdapter

        })

        viewModel.movieMedia.observe(this, Observer {
            setMedia(it)
        })

        viewModel.movieDetails.observe(this, Observer {
            bindUi(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })
    }

    private fun setMedia(it: MediaResponse) {
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
                    this,
                    moviePosters
                ) { posterMedia: ImageView, poster: Poster ->
                    Glide.with(this).load(IMAGE_BASE_URL + poster.filePath).into(posterMedia)
                }
                    .withHiddenStatusBar(false)
                    .show()
            })
        } else {
            Glide.with(this).load(R.color.colorPrimary).into(posterMedia)
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
                    this,
                    movieBackdrops
                ) { backdropMedia: ImageView, backdrop: Backdrop ->
                    Glide.with(this).load(IMAGE_BASE_URL + backdrop.filePath)
                        .into(backdropMedia)
                }
                    .withHiddenStatusBar(false)
                    .show()
            })
        } else {
            Glide.with(this).load(R.color.colorPrimary).into(backdropMedia)
            backdrop_count.text = "No Backdrops"
        }
    }

    private fun bindUi(it: MovieDetail) {

        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
        val date: Date = originalFormat.parse(it.releaseDate)
        val formattedDate: String = targetFormat.format(date)

        title.text = it.title
        releaseDate.text = formattedDate + "  ●"
        runTime.text = "${it.runtime} Min"
        status.text = it.status

        if (!it.overview.isNullOrEmpty()) {
            movieOverview.text = it.overview
        } else {
            synopsisLayout.visibility = View.GONE
        }
        val genre: ArrayList<Genre> = it.genres
        for (i in genre) {
            genresName.append("\u25CF ${i.name}  ")
        }

        if (!it.backdropPath.isNullOrEmpty()) {
            var backDropURL: String = IMAGE_BASE_URL + it.backdropPath
            Glide.with(this)
                .load(backDropURL)
                .into(backdropImage)
        } else {
            Glide.with(this)
                .load(R.drawable.no_image_red)
                .fitCenter()
                .into(backdropImage)
        }

        if (!it.posterPath.isNullOrEmpty()) {
            val moviePosterURL: String = IMAGE_BASE_URL + it.posterPath
            Glide.with(this)
                .load(moviePosterURL)
                .into(posterImage)
        } else {
            Glide.with(this)
                .load(R.drawable.no_image_red)
                .into(posterImage)
        }


    }

    private fun getViewModel(movieId: Int): MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}