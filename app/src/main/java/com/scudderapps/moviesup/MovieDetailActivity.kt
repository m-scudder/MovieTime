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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.adapter.CastListAdapter
import com.scudderapps.moviesup.adapter.TrailerListAdapter
import com.scudderapps.moviesup.api.POSTER_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.CastDetail
import com.scudderapps.moviesup.models.Genre
import com.scudderapps.moviesup.models.MovieDetail
import com.scudderapps.moviesup.models.VideoDetail
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

    @BindView(R.id.likes_count)
    lateinit var likeCount: TextView

    @BindView(R.id.genresName)
    lateinit var genresName: TextView

    @BindView(R.id.runTime)
    lateinit var runTime: TextView

    @BindView(R.id.detail_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.detailProgressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.text_error_movie_detail)
    lateinit var errorTextView: TextView

    @BindView(R.id.trailerListView)
    lateinit var trailerList: RecyclerView

    @BindView(R.id.castListView)
    lateinit var castList: RecyclerView

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailRepository

    private lateinit var trailerAdapter: TrailerListAdapter
    private lateinit var trailerDetail: ArrayList<VideoDetail>

    private lateinit var castAdapter: CastListAdapter
    private lateinit var castDetail: ArrayList<CastDetail>


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
        movieRepository =
            MovieDetailRepository(
                apiService
            )
        viewModel = getViewModel(movieId)

        viewModel.videoDetails.observe(this, Observer {
            trailerDetail = it.videosList
            trailerAdapter = TrailerListAdapter(trailerDetail, this)
            trailerList.layoutManager = linearLayoutManager
            trailerList.setHasFixedSize(true)
            trailerList.adapter = trailerAdapter

        })

        viewModel.castDetails.observe(this, Observer {
            castDetail = it.castDetail
            castAdapter = CastListAdapter(castDetail, this)
            castList.layoutManager = linearLayoutManager2
            castList.setHasFixedSize(true)
            castList.adapter = castAdapter

        })

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

//        val parsedDate =
//            LocalDate.parse(it.releaseDate, DateTimeFormatter.ofPattern("yyyy MMMM d"))

        title.text = it.title
        movieOverview.text = it.overview
        likeCount.text = "\uD83D\uDC4D " + it.voteCount
        runTime.text = it.runtime.toString() + " Min ● " + it.status + " ● " + it.releaseDate

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