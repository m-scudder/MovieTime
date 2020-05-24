package com.scudderapps.moviesup.fragments.tvshowsdetails.tvseasondetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.common.TrailerListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.tv.seasons.SeasonDetailRepository
import com.scudderapps.moviesup.viewmodel.TvSeasonDetailViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TvSeasonAboutFragment(private val tvId: Int, private val seasonNumber: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.tv_season_overview)
    lateinit var tvSeasonOverView: ExpandableTextView

    @BindView(R.id.tv_season_release_date)
    lateinit var tvSeasonReleaseDate: TextView

    @BindView(R.id.tv_season_total_episodes_count)
    lateinit var tvTotalEpisodes: TextView

    @BindView(R.id.tv_season_about_synopsis_layout)
    lateinit var tvSynopsisLayout: LinearLayout

    @BindView(R.id.season_trailer_layout)
    lateinit var tvTrailerLayout: LinearLayout

    @BindView(R.id.trailerListView)
    lateinit var tvSeasonTrailerView: RecyclerView

    private lateinit var seasonViewModel: TvSeasonDetailViewModel
    private lateinit var tvSeasonRepository: SeasonDetailRepository
    private lateinit var trailerAdapter: TrailerListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tv_season_about_fragment, container, false)
        Log.d("tvId", tvId.toString())
        Log.d("seasonNumber", seasonNumber.toString())
        ButterKnife.bind(this, rootView)

        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvSeasonRepository = SeasonDetailRepository(apiService)
        seasonViewModel = getViewModel(tvId, seasonNumber)

        seasonViewModel.tvSeasonDetails.observe(viewLifecycleOwner, Observer {
            if (it.overview.isNotEmpty()) {
                tvSeasonOverView.text = it.overview
            } else {
                tvSynopsisLayout.visibility = View.GONE
            }
            tvTotalEpisodes.text = it.episodes.size.toString()

            if (it.airDate.isNotEmpty()) {
                val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
                val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
                val date: Date = originalFormat.parse(it.airDate)
                val formattedDate: String = targetFormat.format(date)
                tvSeasonReleaseDate.text = formattedDate
            } else {
                tvSeasonReleaseDate.text = "-"
            }
        })

        seasonViewModel.tvSeasonVideos.observe(viewLifecycleOwner, Observer {
            if (!it.videosList.isNullOrEmpty()) {
                trailerAdapter =
                    TrailerListAdapter(
                        it.videosList,
                        rootView.context
                    )
                val linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                tvSeasonTrailerView.layoutManager = linearLayoutManager
                tvSeasonTrailerView.setHasFixedSize(true)
                tvSeasonTrailerView.adapter = trailerAdapter
            } else {
                tvTrailerLayout.visibility = View.GONE
            }
        })
        return rootView
    }

    private fun getViewModel(tvId: Int, seasonNumber: Int): TvSeasonDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TvSeasonDetailViewModel(tvSeasonRepository, tvId, seasonNumber) as T
            }
        })[TvSeasonDetailViewModel::class.java]
    }

}
