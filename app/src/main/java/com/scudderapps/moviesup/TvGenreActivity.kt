package com.scudderapps.moviesup

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.adapter.tvshows.tvdetails.TvPagedListAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.discovergenres.tvshows.TvDiscoverPagedListRepository
import com.scudderapps.moviesup.viewmodel.TvGenreDiscoverViewModel

class TvGenreActivity : AppCompatActivity() {

    @BindView(R.id.tv_genre_view)
    lateinit var tvGenreView: RecyclerView

    @BindView(R.id.tv_genre_toolbar)
    lateinit var tvGenreToolbar: Toolbar

    @BindView(R.id.tv_genre_bar)
    lateinit var tvGenreBar: ProgressBar

    private lateinit var discoverTvViewModel: TvGenreDiscoverViewModel
    lateinit var tvPagedListRepository: TvDiscoverPagedListRepository
    private val discoverTvAdapter =
        TvPagedListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_genre)
        ButterKnife.bind(this)
        setSupportActionBar(tvGenreToolbar)

        val data = intent.extras
        var id = data!!.getInt("id")
        var name = data!!.getString("name")
        supportActionBar!!.title = "$name Shows"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val apiService: ApiInterface = TheTMDBClient.getClient()
        tvPagedListRepository =
            TvDiscoverPagedListRepository(
                apiService
            )

        discoverTvViewModel = discoverTvViewModel(id)
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = discoverTvAdapter.getItemViewType(position)
                return if (viewType == discoverTvAdapter.POPULAR_MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        discoverTvViewModel.discoverTvList.observe(this, Observer {
            discoverTvAdapter.submitList(it)
            tvGenreView.layoutManager = layoutManager
            tvGenreView.setHasFixedSize(true)
            tvGenreView.adapter = discoverTvAdapter
        })

        discoverTvViewModel.networkState.observe(this, Observer {

            tvGenreBar.visibility =
                if (discoverTvViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!discoverTvViewModel.listIsEmpty()) {
                discoverTvAdapter.setNetworkState(it)
            }
        })
    }

    private fun discoverTvViewModel(id: Int): TvGenreDiscoverViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TvGenreDiscoverViewModel(tvPagedListRepository, id) as T
            }
        })[TvGenreDiscoverViewModel::class.java]
    }
}
