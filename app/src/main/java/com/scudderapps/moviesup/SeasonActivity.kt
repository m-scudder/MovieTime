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
import com.scudderapps.moviesup.adapter.tvshows.SeasonDetailTabAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.tv.seasons.SeasonDetailRepository
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.SeasonDetailViewModel
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel

class SeasonActivity : AppCompatActivity() {

    @BindView(R.id.season_backdrop_image)
    lateinit var tvSeasonBackdropImage: ImageView

    @BindView(R.id.tv_season_poster_image)
    lateinit var tvSeasonPosterImage: ImageView

    @BindView(R.id.tv_season_title)
    lateinit var tvSeasonTitle: TextView

    @BindView(R.id.tv_season_name)
    lateinit var tvSeasonName: TextView

    @BindView(R.id.tv_season_toolbar)
    lateinit var tvSeasonToolbar: Toolbar

    @BindView(R.id.tv_season_detail_tab_layout)
    lateinit var tvSeasonDetailTabLayout: TabLayout

    @BindView(R.id.tv_season_detail_viewpager)
    lateinit var tvSeasonDetailViewPager: ViewPager

    private lateinit var viewModel: SeasonDetailViewModel
    private lateinit var tvSeasonRepository: SeasonDetailRepository

    lateinit var tvDetailViewModel: TvDetailViewModel
    lateinit var tvDetailRepository: TvDetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_season)
        ButterKnife.bind(this)
        setSupportActionBar(tvSeasonToolbar)
        supportActionBar!!.title = ""

        val data = intent.extras
        val seasonNumber = data!!.getInt("seasonNumber")
        val tvId = data.getInt("tvId")

        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvSeasonRepository =
            SeasonDetailRepository(
                apiService
            )
        viewModel = getViewModel(tvId, seasonNumber)

        tvDetailRepository =
            TvDetailRepository(
                apiService
            )
        tvDetailViewModel = getTvViewModel(tvId)

        tvSeasonDetailTabLayout.addTab(tvSeasonDetailTabLayout.newTab().setText("Episodes"))
        tvSeasonDetailTabLayout.addTab(tvSeasonDetailTabLayout.newTab().setText("About"))
        tvSeasonDetailTabLayout.addTab(tvSeasonDetailTabLayout.newTab().setText("Cast"))

        val adapter =
            SeasonDetailTabAdapter(
                seasonNumber,
                this,
                supportFragmentManager,
                tvSeasonDetailTabLayout.tabCount
            )
        tvSeasonDetailViewPager.adapter = adapter

        tvDetailViewModel.tvDetails.observe(this, Observer {
            tvSeasonName.text = it.name
            if (!it.backdropPath.isNullOrEmpty()) {
                var backDropURL: String = IMAGE_BASE_URL + it.backdropPath
                Glide.with(this)
                    .load(backDropURL)
                    .into(tvSeasonBackdropImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.no_image_found)
                    .centerInside()
                    .into(tvSeasonBackdropImage)
            }
        })

        viewModel.tvSeasonDetails.observe(this, Observer {
            tvSeasonTitle.text = it.name

            if (!it.posterPath.isNullOrEmpty()) {
                val moviePosterURL: String = IMAGE_BASE_URL + it.posterPath
                Glide.with(this)
                    .load(moviePosterURL)
                    .into(tvSeasonPosterImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.no_image_found)
                    .centerInside()
                    .into(tvSeasonPosterImage)
            }
        })

    }

    private fun getViewModel(tvId: Int, seasonNumber: Int): SeasonDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SeasonDetailViewModel(tvSeasonRepository, tvId, seasonNumber) as T
            }
        })[SeasonDetailViewModel::class.java]
    }

    private fun getTvViewModel(tvId: Int): TvDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TvDetailViewModel(tvDetailRepository, tvId) as T
            }
        })[TvDetailViewModel::class.java]
    }
}
