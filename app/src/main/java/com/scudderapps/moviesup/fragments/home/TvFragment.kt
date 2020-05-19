package com.scudderapps.moviesup.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.home.TvTabAdapter

class TvFragment: Fragment() {

    lateinit var tvTabLayout: TabLayout
    lateinit var tvViewPager: ViewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tv_fragment, container, false)

        tvTabLayout = view.findViewById(R.id.tv_tab_layout)
        tvViewPager = view.findViewById(R.id.tv_viewpager)
        tvTabLayout.addTab(tvTabLayout.newTab().setText("Now Playing"))
        tvTabLayout.addTab(tvTabLayout.newTab().setText("On Air Today"))
        tvTabLayout.addTab(tvTabLayout.newTab().setText("Popular"))

        val adapter =
            TvTabAdapter(
                view.context,
                fragmentManager!!,
                tvTabLayout.tabCount
            )
        tvViewPager.adapter = adapter

        tvViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                tvTabLayout
            )
        )
        tvTabLayout.setupWithViewPager(tvViewPager)

        return view
    }

    override fun onPause() {
        super.onPause()
    }
}