package com.scudderapps.moviesup.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.home.WatchlistTabAdapter

class WatchlistFragment : Fragment() {

    @BindView(R.id.watchlist_tab_layout)
    lateinit var watchlistTabLayout: TabLayout

    @BindView(R.id.watchlist_list_pager)
    lateinit var watchlistViewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.watchlist_fragment, container, false)
        ButterKnife.bind(this, view)
        watchlistTabLayout.addTab(watchlistTabLayout.newTab().setText("Movies"))
        watchlistTabLayout.addTab(watchlistTabLayout.newTab().setText("Shows"))

        val adapter =
            WatchlistTabAdapter(
                view.context,
                fragmentManager!!,
                watchlistTabLayout.tabCount
            )
        watchlistViewPager.adapter = adapter

        watchlistViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                watchlistTabLayout
            )
        )
        watchlistTabLayout.setupWithViewPager(watchlistViewPager)

        return view
    }
}