package com.scudderapps.moviesup.adapter


import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.MovieDetailActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.models.Cast
import kotlinx.android.synthetic.main.movie_list_item.view.*
import android.util.Pair as UtilPair

class MovieAdapter(private val movies: List<Cast>, private val context: Context) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val itemVideo = movies[position]
        holder.bindVideos(itemVideo, context)
    }

    override fun getItemCount() = movies.size

    class MovieHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var movie: Cast? = null
        private lateinit var context: Context

        fun bindVideos(movies: Cast, context: Context) {
            this.movie = movies
            this.context = context
            Log.d("movie", movie.toString())
            view.title.text = movie?.title
            view.rating_view.text = "\uD83C\uDF1F " + movie?.voteAverage.toString()

            if (!movie?.posterPath.isNullOrEmpty()) {
                val posterUrl = IMAGE_BASE_URL + movie?.posterPath
                Glide.with(itemView.context)
                    .load(posterUrl)
                    .into(view.movie_image)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.no_image_found)
                    .centerInside()
                    .into(view.movie_image)
            }

            itemView.setOnClickListener {

                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", movie?.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    UtilPair<View, String>(itemView.itemCard, "imageTransition"),
                    UtilPair<View, String>(itemView.title, "titleTransition")
                )
                context.startActivity(intent, options.toBundle())

            }
        }
    }
}