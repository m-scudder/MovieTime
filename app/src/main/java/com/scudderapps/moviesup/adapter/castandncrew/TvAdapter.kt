package com.scudderapps.moviesup.adapter.castandncrew

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.TvDetailActivity
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.models.main.TV
import kotlinx.android.synthetic.main.movie_list_item.view.*

class TvAdapter(private val tv: List<TV>, private val context: Context) :
    RecyclerView.Adapter<TvAdapter.TvHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
        return TvHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: TvHolder, position: Int) {
        val tvItem = tv[position]
        holder.bindVideos(tvItem, context)
    }

    override fun getItemCount() = tv.size

    class TvHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var tv: TV? = null
        private lateinit var context: Context

        fun bindVideos(tv: TV, context: Context) {
            this.tv = tv
            this.context = context
            Log.d("movie", tv.toString())
            view.title.text = tv?.name
            view.rating_view.text = "\uD83C\uDF1F " + tv?.voteAverage.toString()

            if (!tv?.posterPath.isNullOrEmpty()) {
                val posterUrl = IMAGE_BASE_URL + tv?.posterPath
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

                val intent = Intent(context, TvDetailActivity::class.java)
                intent.putExtra("id", tv?.id)
                context.startActivity(intent)

            }
        }
    }
}