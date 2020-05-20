package com.scudderapps.moviesup.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.tvshows.TvPagedListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.tv.TvPagedListRepository
import com.scudderapps.moviesup.viewmodel.TvListViewModel

class TvListFragment(private val type: String) : Fragment() {

    private lateinit var tvView: RecyclerView
    private lateinit var tvProgressBar: ProgressBar
    private lateinit var rootView: View
    private lateinit var tvAdapter: TvPagedListAdapter
    private lateinit var listViewModel: TvListViewModel
    lateinit var tvPagedListRepository: TvPagedListRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvAdapter =
            TvPagedListAdapter(
                activity!!.applicationContext
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tv_list_fragment, container, false)
        tvView = rootView.findViewById(R.id.tv_list_view)
        tvProgressBar = rootView.findViewById(R.id.tv_progress_bar)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvPagedListRepository = TvPagedListRepository(apiService)
        listViewModel = tvListViewModel(type)
        populatingViews()

        return rootView
    }

    private fun tvListViewModel(type: String): TvListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TvListViewModel(tvPagedListRepository, type) as T
            }
        })[TvListViewModel::class.java]
    }

    private fun populatingViews() {
        listViewModel.tvPagedList.observe(this, Observer {
            tvAdapter.submitList(it)
            val layoutManager = GridLayoutManager(activity, 4)
            tvView.layoutManager = layoutManager
            tvView.setHasFixedSize(true)
            tvView.adapter = tvAdapter

        })

        listViewModel.networkState.observe(this, Observer {

            tvProgressBar.visibility =
                if (listViewModel.movieListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!listViewModel.movieListIsEmpty()) {
                tvAdapter.setNetworkState(it)
            }
        })
    }

}