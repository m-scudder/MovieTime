package com.scudderapps.moviesup.fragments.cast

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.castandncrew.TvAdapter
import com.scudderapps.moviesup.api.ApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.cast.CastDetailRepository
import com.scudderapps.moviesup.viewmodel.CastDetailViewModel

class CastShowsFragment(private val castId: Int) : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.cast_tv_shows_list)
    lateinit var castShowsList: RecyclerView

    @BindView(R.id.cast_show_no_image_found)
    lateinit var noImageFound: ImageView

    private lateinit var tvAdapter: TvAdapter
    lateinit var peopleDetailRepository: CastDetailRepository
    private lateinit var peopleDetailViewModel: CastDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.cast_shows_fragment, container, false)
        ButterKnife.bind(this, rootView)
        val apiService: ApiInterface = TheTMDBClient.getClient()
        peopleDetailRepository =
            CastDetailRepository(
                apiService
            )
        peopleDetailViewModel = castShowListViewModel(castId)

        peopleDetailViewModel.tvCredits.observe(viewLifecycleOwner, Observer {

            if (!it.cast.isNullOrEmpty()) {
                tvAdapter = TvAdapter(
                    it.cast,
                    rootView.context
                )
                val layoutManager = GridLayoutManager(activity, 3)
                castShowsList.layoutManager = layoutManager
                castShowsList.setHasFixedSize(true)
                castShowsList.adapter = tvAdapter
            } else {
                noImageFound.visibility = View.VISIBLE
            }
        })

        return rootView
    }

    private fun castShowListViewModel(castId: Int): CastDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CastDetailViewModel(peopleDetailRepository, castId) as T
            }
        })[CastDetailViewModel::class.java]
    }
}
