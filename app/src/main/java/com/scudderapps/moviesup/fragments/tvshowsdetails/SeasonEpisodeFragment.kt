package com.scudderapps.moviesup.fragments.tvshowsdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.scudderapps.moviesup.R

class SeasonEpisodeFragment(val seasonId: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.tv_season_episode_fragment, container, false)
    }

}
