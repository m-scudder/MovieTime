package com.scudderapps.moviesup.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.MovieTabAdapter

class MovieFragment() : Fragment() {

    lateinit var movieTabLayout: TabLayout
    lateinit var movieViewPager: ViewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_fragment, container, false)

        movieTabLayout = view.findViewById(R.id.movie_tab_layout)
        movieViewPager = view.findViewById(R.id.movie_list_pager)
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Top Rated"))
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Popular"))
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Now Playing"))
        movieTabLayout.addTab(movieTabLayout.newTab().setText("Upcoming"))

        val adapter =
            MovieTabAdapter(view.context, fragmentManager!!, movieTabLayout.tabCount)
        movieViewPager.adapter = adapter

        movieViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                movieTabLayout
            )
        )

        movieTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.v("Name", tab.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }

    override fun onPause() {
        super.onPause()
    }
}