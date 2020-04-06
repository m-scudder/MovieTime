package com.scudderapps.moviesup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.POSTER_BASE_URL
import com.scudderapps.moviesup.models.CastDetail
import kotlinx.android.synthetic.main.cast_list_item.view.*

class CastListAdapter(private val cast: ArrayList<CastDetail>, private val context: Context) :
    RecyclerView.Adapter<CastListAdapter.CastHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.cast_list_item, parent, false)
        return CastHolder(view)
    }

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        val castItem = cast[position]
        holder.bindVideos(castItem, context)
    }

    override fun getItemCount() = cast.size

    class CastHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var cast: CastDetail? = null
        private lateinit var context: Context

        fun bindVideos(cast: CastDetail, context: Context) {
            this.cast = cast
            this.context = context
            itemView.cast_name.text = cast.name
            val profileUrl = POSTER_BASE_URL + cast?.profilePath
            Glide.with(view)
                .load(profileUrl)
                .placeholder(R.drawable.default_avatar)
                .into(itemView.cast_image)

            itemView.setOnClickListener(View.OnClickListener {

            })
        }
    }
}