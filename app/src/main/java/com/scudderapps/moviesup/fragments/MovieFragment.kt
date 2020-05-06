package com.scudderapps.moviesup.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.R

class MovieFragment() : Fragment() {

    @BindView(R.id.movie_tab_layout)
    lateinit var movieTabLayout: TabLayout

    @BindView(R.id.movie_viewpager)
    lateinit var movieViewPage: ViewPager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_fragment, container, false)

        movieTabLayout = TabLayout(view.context)
        movieViewPage = ViewPager(view.context)

        movieTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                movieViewPage.currentItem = tab.position
                Log.v("Clicked", tab.position.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }
}