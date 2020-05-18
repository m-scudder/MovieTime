package com.scudderapps.moviesup.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scudderapps.moviesup.DiscoverMovie
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.models.main.Genre
import kotlinx.android.synthetic.main.known_as_item.view.*

class GenreListAdapter(private val details: List<Genre>, private val context: Context) :
    RecyclerView.Adapter<GenreListAdapter.GenreHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.known_as_item, parent, false)
        return GenreHolder(view)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        val genresItem = details[position]
        holder.bindVideos(genresItem, context)
    }

    override fun getItemCount() = details.size

    class GenreHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private lateinit var genres: Genre
        private lateinit var context: Context

        fun bindVideos(genre: Genre, context: Context) {
            this.genres = genre
            this.context = context
            view.also_known_as.text = genres.name

            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, DiscoverMovie::class.java)
                intent.putExtra("id", genre?.id)
                intent.putExtra("name", genre?.name)
                context.startActivity(intent)
            })
        }
    }
}