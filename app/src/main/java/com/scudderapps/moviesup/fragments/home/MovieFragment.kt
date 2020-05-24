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
import com.scudderapps.moviesup.adapter.home.MovieTabAdapter

class MovieFragment() : Fragment() {

    @BindView(R.id.movie_tab_layout)
    lateinit var movieTabLayout: TabLayout

    @BindView(R.id.movie_list_pager)
    lateinit var movieViewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_main_fragment, container, false)
        ButterKnife.bind(this, view)
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Now Playing"))
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Popular"))
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Top Rated"))
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Upcoming"))

        val adapter =
            MovieTabAdapter(
                view.context,
                fragmentManager!!,
                movieTabLayout.tabCount
            )
        movieViewPager.adapter = adapter

        movieViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                movieTabLayout
            )
        )
        movieTabLayout.setupWithViewPager(movieViewPager)
        return view
    }
}