package com.scudderapps.moviesup

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.adapter.tvshows.TvDetailTabAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.tv.TvDetail
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TvDetailActivity : AppCompatActivity() {

    @BindView(R.id.tv_backdrop_image)
    lateinit var tvBackdropImage: ImageView

    @BindView(R.id.tv_poster_image)
    lateinit var tvPosterImage: ImageView

    @BindView(R.id.tv_title)
    lateinit var tvTitle: TextView

    @BindView(R.id.tv_releaseDate)
    lateinit var tvReleaseDate: TextView

    @BindView(R.id.tv_status)
    lateinit var tvStatus: TextView

    @BindView(R.id.tv_detail_toolbar)
    lateinit var tvToolbar: Toolbar

    @BindView(R.id.tv_detail_tab_layout)
    lateinit var tvDetailTabLayout: TabLayout

    @BindView(R.id.tv_detail_viewpager)
    lateinit var tvDetailViewPager: ViewPager

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
        tvRepository =
            TvDetailRepository(
                apiService
            )
        viewModel = getViewModel(tvId)

        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("About"))
        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("Seasons"))
        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("Cast"))
        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("Crew"))
        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("Reviews"))
        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("Similar"))
        tvDetailTabLayout.addTab(tvDetailTabLayout.newTab().setText("Recommended"))

        val adapter =
            TvDetailTabAdapter(
                tvId,
                this,
                supportFragmentManager,
                tvDetailTabLayout.tabCount
            )
        tvDetailViewPager.adapter = adapter

        tvDetailTabLayout.setupWithViewPager(tvDetailViewPager)
        tvDetailViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                tvDetailTabLayout
            )
        )
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
            tvReleaseDate.text = formattedDate
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