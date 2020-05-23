package com.scudderapps.moviesup.fragments.tvshowsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.tvshows.SeasonEpisodeListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.tv.Episode
import com.scudderapps.moviesup.repository.tv.seasons.SeasonDetailRepository
import com.scudderapps.moviesup.viewmodel.SeasonDetailViewModel

class SeasonEpisodeFragment(val tvId: Int, val seasonNumber: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.tv_episode_list)
    lateinit var tvSeasonEpisodeListView: RecyclerView

    @BindView(R.id.episodeCount)
    lateinit var episodeCount: TextView

    private lateinit var tvSeasonEpisodeListAdapter: SeasonEpisodeListAdapter
    private lateinit var tvSeasonsEpisodeList: List<Episode>

    private lateinit var seasonViewModel: SeasonDetailViewModel
    private lateinit var tvSeasonRepository: SeasonDetailRepository
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tv_season_episode_fragment, container, false)
        ButterKnife.bind(this, rootView)

        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvSeasonRepository = SeasonDetailRepository(apiService)
        seasonViewModel = getViewModel(tvId, seasonNumber)
        seasonViewModel.tvSeasonDetails.observe(viewLifecycleOwner, Observer {
            tvSeasonsEpisodeList = it.episodes
            episodeCount.text = "Total episodes :" + it.episodes.size.toString()
            tvSeasonEpisodeListAdapter =
                SeasonEpisodeListAdapter(tvSeasonsEpisodeList, rootView.context)
            linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            tvSeasonEpisodeListView.layoutManager = linearLayoutManager
            tvSeasonEpisodeListView.setHasFixedSize(true)
            tvSeasonEpisodeListView.adapter = tvSeasonEpisodeListAdapter

        })
        return rootView
    }

    private fun getViewModel(tvId: Int, seasonNumber: Int): SeasonDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SeasonDetailViewModel(tvSeasonRepository, tvId, seasonNumber) as T
            }
        })[SeasonDetailViewModel::class.java]
    }

}
