package com.scudderapps.moviesup.fragments.tvshowsdetails

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
import com.scudderapps.moviesup.adapter.movie.moviedetails.CrewListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.common.CrewDetail
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel

class TvCrewFragment(val tvId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.tv_crew_list)
    lateinit var tvCrewView: RecyclerView

    @BindView(R.id.no_tv_crew_found)
    lateinit var noTvCrewImage: ImageView

    private lateinit var crewAdapter: CrewListAdapter
    private lateinit var crewDetailList: ArrayList<CrewDetail>

    private lateinit var viewModel: TvDetailViewModel
    private lateinit var tvRepository: TvDetailRepository
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.tv_crew_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvRepository = TvDetailRepository(apiService)
        viewModel = getViewModel(tvId)
        viewModel.tvCast.observe(viewLifecycleOwner, Observer {
            if (!it.crewDetail.isNullOrEmpty()) {
                crewDetailList = it.crewDetail
                crewAdapter = CrewListAdapter(crewDetailList, rootView.context)
                linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                tvCrewView.layoutManager = linearLayoutManager
                tvCrewView.setHasFixedSize(true)
                tvCrewView.adapter = crewAdapter
            } else{
                noTvCrewImage.visibility = View.VISIBLE
            }

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
