package com.scudderapps.moviesup.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.MovieDetailActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.POSTER_BASE_URL
import com.scudderapps.moviesup.models.Movie
import com.scudderapps.moviesup.repository.NetworkState
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import android.util.Pair as UtilPair

class MoviePageListAdapter(private val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    private val POPULAR_MOVIE_VIEW_TYPE = 1
    private val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == POPULAR_MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            MovieItemVieHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            MovieItemVieHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == POPULAR_MOVIE_VIEW_TYPE) {
            (holder as MovieItemVieHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    fun hasAnyExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasAnyExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasAnyExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            POPULAR_MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemVieHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie?, context: Context) {
            itemView.title.text = movie?.title
            itemView.rating_view.text = "\uD83C\uDF1F " + movie?.voteAverage.toString()

            val posterUrl = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .placeholder(R.drawable.icon)
                .into(itemView.movie_image)

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

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(netwrokState: NetworkState?) {
            if (netwrokState != null && netwrokState == NetworkState.LOADING) {
                itemView.networkStateBar.visibility = View.VISIBLE
            } else {
                itemView.networkStateBar.visibility = View.GONE
            }
            if (netwrokState != null && netwrokState == NetworkState.ERROR) {
                itemView.text_error_state.visibility = View.VISIBLE
                itemView.text_error_state.text = netwrokState.msg
            } else if (netwrokState != null && netwrokState == NetworkState.ENDOFLIST) {
                itemView.text_error_state.visibility = View.VISIBLE
                itemView.text_error_state.text = netwrokState.msg
            } else {
                itemView.text_error_state.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasAnyExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasAnyExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}