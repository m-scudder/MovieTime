package com.scudderapps.moviesup.fragments.tvshowsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.scudderapps.moviesup.adapter.tvshows.TvSeasonListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.tv.Season
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel

class TvSeasonsFragment(val tvId: Int) : Fragment() {
    private lateinit var rootView: View


    @BindView(R.id.tv_season_list)
    lateinit var tvSeasonListView: RecyclerView

    private lateinit var tvSeasonAdapter: TvSeasonListAdapter
    private lateinit var tvSeasonsList: List<Season>

    private lateinit var viewModel: TvDetailViewModel
    private lateinit var tvRepository: TvDetailRepository
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tv_seasons_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvRepository = TvDetailRepository(apiService)
        viewModel = getViewModel(tvId)
        viewModel.tvDetails.observe(viewLifecycleOwner, Observer {
            tvSeasonsList = it.seasons
            tvSeasonAdapter = TvSeasonListAdapter(tvId, tvSeasonsList, rootView.context)
            linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            tvSeasonListView.layoutManager = linearLayoutManager
            tvSeasonListView.setHasFixedSize(true)
            tvSeasonListView.adapter = tvSeasonAdapter

        })
        return rootView
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
