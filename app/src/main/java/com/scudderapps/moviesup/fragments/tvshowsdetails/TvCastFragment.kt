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
import com.scudderapps.moviesup.adapter.common.CastListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.common.CastDetail
import com.scudderapps.moviesup.repository.tv.tvdetails.TvDetailRepository
import com.scudderapps.moviesup.viewmodel.TvDetailViewModel

class TvCastFragment(val tvId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.tv_cast_list)
    lateinit var tvCastListView: RecyclerView

    @BindView(R.id.no_tv_cast_found)
    lateinit var noTvCastImage: ImageView

    private lateinit var castAdapter: CastListAdapter
    private lateinit var castDetail: ArrayList<CastDetail>

    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: TvDetailViewModel
    private lateinit var tvRepository: TvDetailRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tv_cast_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvRepository = TvDetailRepository(apiService)
        viewModel = getViewModel(tvId)
        viewModel.tvCast.observe(viewLifecycleOwner, Observer {
            if (!it.castDetail.isNullOrEmpty()) {
                castDetail = it.castDetail
                castAdapter =
                    CastListAdapter(
                        castDetail,
                        rootView.context
                    )
                linearLayoutManager = LinearLayoutManager(activity)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                tvCastListView.layoutManager = linearLayoutManager
                tvCastListView.setHasFixedSize(true)
                tvCastListView.adapter = castAdapter
            } else {
                noTvCastImage.visibility = View.VISIBLE
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
