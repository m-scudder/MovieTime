package com.scudderapps.moviesup.fragments.tvshowsdetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.movie.moviedetails.TrailerListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.movie.Backdrop
import com.scudderapps.moviesup.models.movie.Poster
import com.scudderapps.moviesup.models.tv.Genre
import com.scudderapps.moviesup.models.tv.ProductionCompany
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TvAboutFragment(private val tvId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.tv_overview)
    lateinit var tvOverview: ExpandableTextView

    @BindView(R.id.tv_genresName)
    lateinit var tvGenresName: TextView

    @BindView(R.id.tv_about_status)
    lateinit var tvAboutStatus: TextView

    @BindView(R.id.tv_show_type)
    lateinit var tvShowType: TextView

    @BindView(R.id.tv_total_seasons)
    lateinit var tvTotalSeasons: TextView

    @BindView(R.id.tv_total_episodes)
    lateinit var tvTotalEpisodes: TextView

    @BindView(R.id.tv_total_runtime)
    lateinit var tvTotalRuntime: TextView

    @BindView(R.id.first_episode_aired)
    lateinit var firstEpisodeAired: TextView

    @BindView(R.id.last_episode_aired)
    lateinit var lastEpisodeAired: TextView

    @BindView(R.id.tv_production_companies)
    lateinit var tvProductionCompanies: TextView

    @BindView(R.id.tv_backdropMedia)
    lateinit var tvBackdropImage: ImageView

    @BindView(R.id.tv_posterMedia)
    lateinit var tvPosterImage: ImageView

    @BindView(R.id.tv_posterCount)
    lateinit var tvPosterCount: TextView

    @BindView(R.id.tv_backdropCount)
    lateinit var tvBackdropCount: TextView

    @BindView(R.id.tv_synopsis_layout)
    lateinit var tvSynopsisLayout: LinearLayout

    @BindView(R.id.tv_genres_layout)
    lateinit var tvGenresLayout: LinearLayout

    @BindView(R.id.tv_trailer_layout)
    lateinit var tvTrailerLayout: LinearLayout

    @BindView(R.id.tv_trailerListView)
    lateinit var tvTrailerListView: RecyclerView

    private lateinit var viewModel: TvDetailViewModel
    private lateinit var tvRepository: TvDetailRepository

    private lateinit var trailerAdapter: TrailerListAdapter
    private lateinit var tvBackdropList: List<Backdrop>
    private lateinit var tvPosterList: List<Poster>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tv_about_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvRepository =
            TvDetailRepository(
                apiService
            )
        viewModel = getViewModel(tvId)
        populatingView()
        return rootView
    }


    @SuppressLint("SetTextI18n")
    private fun populatingView() {

        viewModel.tvDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (!it.status.isNullOrEmpty())
                tvAboutStatus.text = it.status else tvAboutStatus.text = "-"

            if (!it.type.isNullOrEmpty())
                tvShowType.text = it.type else tvShowType.text = "-"

            if (!it.numberOfSeasons.toString().isNullOrEmpty())
                tvTotalSeasons.text = it.numberOfSeasons.toString() else tvTotalSeasons.text = "-"

            if (!it.numberOfEpisodes.toString().isNullOrEmpty())
                tvTotalEpisodes.text = it.numberOfEpisodes.toString() else tvTotalEpisodes.text =
                "-"

            val runTimeList :List<Int> = it.episodeRunTime
            if (!runTimeList.isNullOrEmpty()) {
                tvTotalRuntime.text =
                    runTimeList[0].toString() + " Min"
            } else {
                tvTotalRuntime.text = "-"
            }

            if (!it.firstAirDate.isNullOrEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(it.firstAirDate)
                val formattedDate: String = targetFormat.format(date)
                firstEpisodeAired.text = formattedDate
            } else {
                firstEpisodeAired.text = "-"
            }

            if (!it.lastAirDate.isNullOrEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(it.lastAirDate)
                val formattedDate: String = targetFormat.format(date)
                lastEpisodeAired.text = formattedDate
            } else {
                lastEpisodeAired.text = "-"
            }

            val tvProductionCompaniesList: List<ProductionCompany> = it.productionCompanies
            if (!tvProductionCompaniesList.isNullOrEmpty()) {
                for (i in tvProductionCompaniesList) {
                    tvProductionCompanies.text = it.productionCompanies[0].name
                }
            } else {
                tvProductionCompanies.text = "-"
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
        })

        viewModel.tvVideoDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (!it.videosList.isNullOrEmpty()) {
                trailerAdapter = TrailerListAdapter(it.videosList, rootView.context)
                val linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                tvTrailerListView.layoutManager = linearLayoutManager
                tvTrailerListView.setHasFixedSize(true)
                tvTrailerListView.adapter = trailerAdapter
            } else {
                tvTrailerLayout.visibility = View.GONE
            }

        })

        viewModel.tvMedia.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tvBackdropList = it.backdrops
            tvPosterList = it.posters
            if (!tvPosterList.isNullOrEmpty()) {
                var mediaPosterURL = tvPosterList[0].filePath
                Log.d("PosterUrl", mediaPosterURL)
                val mediaPosterUrl: String = IMAGE_BASE_URL + mediaPosterURL
                Glide.with(this).load(mediaPosterUrl).into(tvPosterImage)
                if (tvPosterList.size == 1) {
                    tvPosterCount.text = tvPosterList.size.toString() + " Poster"
                } else {
                    tvPosterCount.text = tvPosterList.size.toString() + " Posters"
                }
                tvPosterImage.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        tvPosterList
                    ) { posterMedia: ImageView, poster: Poster ->
                        Glide.with(this).load(IMAGE_BASE_URL + poster.filePath)
                            .into(posterMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside()
                    .into(tvPosterImage)
                tvPosterCount.text = "No Posters"
            }
            if (!tvBackdropList.isNullOrEmpty()) {
                var mediaBackdropURL = tvBackdropList[0].filePath
                Log.d("PosterUrl", mediaBackdropURL)
                val mediaBackdropUrl: String = IMAGE_BASE_URL + mediaBackdropURL
                Glide.with(this).load(mediaBackdropUrl).into(tvBackdropImage)
                if (tvBackdropList.size == 1) {
                    tvBackdropCount.text = tvBackdropList.size.toString() + " Backdrop"
                } else {
                    tvBackdropCount.text = tvBackdropList.size.toString() + " Backdrops"
                }

                tvBackdropImage.setOnClickListener(View.OnClickListener {
                    StfalconImageViewer.Builder(
                        activity,
                        tvBackdropList
                    ) { backdropMedia: ImageView, backdrop: Backdrop ->
                        Glide.with(this).load(IMAGE_BASE_URL + backdrop.filePath)
                            .into(backdropMedia)
                    }
                        .withHiddenStatusBar(false)
                        .show()
                })
            } else {
                Glide.with(this).load(R.drawable.no_image_found).centerInside()
                    .into(tvBackdropImage)
                tvBackdropCount.text = "No Backdrops"
            }
        })
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
