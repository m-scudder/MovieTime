package com.scudderapps.moviesup.fragments.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.scudderapps.moviesup.R

class MovieCastFragment : Fragment() {

    private lateinit var rootView: View

    @BindView(R.id.movie_cast_list)
    lateinit var castListView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.cast_movie_fragment, container, false)
        return rootView
    }

}
