package com.scudderapps.moviesup.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.MovieDetailActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.models.Movie
import kotlinx.android.synthetic.main.search_list_item.view.*

class SearchListAdapter(private val movies: ArrayList<Movie>, private val context: Context) :
    RecyclerView.Adapter<SearchListAdapter.SearchListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.search_list_item, parent, false)
        return SearchListHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListHolder, position: Int) {
        val itemVideo = movies[position]
        holder.bindMovieData(itemVideo, context)
    }

    override fun getItemCount() = movies.size

    class SearchListHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bindMovieData(movie: Movie?, context: Context) {
            itemView.search_title.text = movie?.title
            itemView.search_rating_view.text = "\uD83C\uDF1F " + movie?.voteAverage.toString()

            if (!movie?.posterPath.isNullOrEmpty()) {
                val posterUrl = IMAGE_BASE_URL + movie?.posterPath
                Glide.with(itemView.context)
                    .load(posterUrl)
                    .into(itemView.search_image)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.no_image_found)
                    .into(itemView.search_image)
            }

            itemView.setOnClickListener {

                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", movie?.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    Pair<View, String>(itemView.searchCard, "imageTransition"),
                    Pair<View, String>(itemView.search_title, "titleTransition")
                )
                context.startActivity(intent, options.toBundle())

            }
        }
    }
}