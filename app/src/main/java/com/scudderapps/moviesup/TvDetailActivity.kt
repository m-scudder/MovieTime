package com.scudderapps.moviesup

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.tv.Genre
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.tv.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TvDetailActivity : AppCompatActivity() {

    @BindView(R.id.tv_backdrop_image)
    lateinit var tvBackdropImage: ImageView

    @BindView(R.id.tv_poster_image)
    lateinit var tvPosterImage: ImageView

    @BindView(R.id.tv_overview)
    lateinit var tvOverview: ExpandableTextView

    @BindView(R.id.tv_title)
    lateinit var tvTitle: TextView

    @BindView(R.id.tv_releaseDate)
    lateinit var tvReleaseDate: TextView

    @BindView(R.id.tv_status)
    lateinit var tvStatus: TextView

    @BindView(R.id.tv_genresName)
    lateinit var tvGenresName: TextView

    @BindView(R.id.tv_detail_toolbar)
    lateinit var tvToolbar: Toolbar

    @BindView(R.id.tv_DetailBar)
    lateinit var tvProgressBar: ProgressBar

    @BindView(R.id.tv_synopsis_layout)
    lateinit var tvSynopsisLayout: LinearLayout

    @BindView(R.id.tv_genres_layout)
    lateinit var tvGenresLayout: LinearLayout

    private lateinit var viewModel: TvDetailViewModel
    private lateinit var tvRepository: TvDetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_detail)
        ButterKnife.bind(this)
        setSupportActionBar(tvToolbar)
        supportActionBar!!.setTitle("")

        val data = intent.extras
        var tvId = data!!.getInt("id")

        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvRepository = TvDetailRepository(apiService)
        viewModel = getViewModel(tvId)

        viewModel.tvDetails.observe(this, androidx.lifecycle.Observer {
            bindUi(it)
        })
    }

    private fun bindUi(it: TvDetail) {
        tvTitle.text = it.name
        tvStatus.text = it.status

        if (!it.firstAirDate.isNullOrEmpty()) {
            val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
            val date: Date = originalFormat.parse(it.firstAirDate)
            val formattedDate: String = targetFormat.format(date)
            tvReleaseDate.text = formattedDate + "  ●"
        }

        if (!it.overview.isNullOrEmpty()) {
            tvOverview.text = it.overview
        } else {
            tvSynopsisLayout.visibility = View.GONE
        }
        val genre: ArrayList<Genre> = it.genres
        if (!genre.isNullOrEmpty()) {
            for (i in genre) {
                tvGenresName.append("\u25CF ${i.name}  ")
            }
        } else {
            tvGenresLayout.visibility = View.GONE
        }


        if (!it.backdropPath.isNullOrEmpty()) {
            var backDropURL: String = IMAGE_BASE_URL + it.backdropPath
            Glide.with(this)
                .load(backDropURL)
                .into(tvBackdropImage)
        } else {
            Glide.with(this)
                .load(R.drawable.no_image_found)
                .centerInside()
                .into(tvBackdropImage)
        }

        if (!it.posterPath.isNullOrEmpty()) {
            val moviePosterURL: String = IMAGE_BASE_URL + it.posterPath
            Glide.with(this)
                .load(moviePosterURL)
                .into(tvPosterImage)
        } else {
            Glide.with(this)
                .load(R.drawable.no_image_found)
                .centerInside()
                .into(tvPosterImage)
        }
    }

    private fun getViewModel(tvId: Int): TvDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TvDetailViewModel(tvRepository, tvId) as T
            }
        })[TvDetailViewModel::class.java]
    }
}