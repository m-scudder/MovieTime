package com.scudderapps.moviesup.fragments.tvshowsdetails.tvseasondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.scudderapps.moviesup.adapter.common.CastListAdapter
import com.scudderapps.moviesup.api.TmdbApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.common.CastDetail
import com.scudderapps.moviesup.repository.tv.seasons.SeasonDetailRepository
import com.scudderapps.moviesup.viewmodel.TvSeasonDetailViewModel

class TvSeasonCastFragment(private val tvId: Int, private val seasonNumber: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.tv_season_cast_list)
    lateinit var tvSeasonCastListView: RecyclerView

    @BindView(R.id.no_tv_season_cast_found)
    lateinit var noTvSeasonCastImage: ImageView

    private lateinit var tvSeasonCastAdapter: CastListAdapter
    private lateinit var castDetail: ArrayList<CastDetail>
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var seasonViewModel: TvSeasonDetailViewModel
    private lateinit var tvSeasonRepository: SeasonDetailRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.tv_season_cast_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: TmdbApiInterface = TheTMDBClient.getClient()
        tvSeasonRepository = SeasonDetailRepository(apiService)
        seasonViewModel = getViewModel(tvId, seasonNumber)

        seasonViewModel.tvSeasonCast.observe(viewLifecycleOwner, Observer {
            if (!it.castDetail.isNullOrEmpty()) {
                tvSeasonCastAdapter =
                    CastListAdapter(
                        it.castDetail,
                        rootView.context
                    )
                val linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                tvSeasonCastListView.layoutManager = linearLayoutManager
                tvSeasonCastListView.setHasFixedSize(true)
                tvSeasonCastListView.adapter = tvSeasonCastAdapter
            } else {
                noTvSeasonCastImage.visibility = View.VISIBLE
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
