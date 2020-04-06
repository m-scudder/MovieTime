package com.scudderapps.moviesup.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.TRAILER_THUMBNAIL_BASE_URL
import com.scudderapps.moviesup.api.TRAILER_THUMBNAIL_END_URL
import com.scudderapps.moviesup.models.VideoDetail
import kotlinx.android.synthetic.main.trailers.view.*


class TrailerListAdapter(private val videos: ArrayList<VideoDetail>, private val context: Context) :
    RecyclerView.Adapter<TrailerListAdapter.TrailerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.trailers, parent, false)
        return TrailerHolder(view)
    }

    override fun onBindViewHolder(holder: TrailerListAdapter.TrailerHolder, position: Int) {
        val itemVideo = videos[position]
        holder.bindVideos(itemVideo, context)
    }

    override fun getItemCount() = videos.size

    class TrailerHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var video: VideoDetail? = null
        private lateinit var context: Context
        lateinit var key: String

        fun bindVideos(videos: VideoDetail, context: Context) {
            this.video = videos
            this.context = context
            key = video!!.key
            view.trailerName.text = video!!.name
            Glide.with(view).load(TRAILER_THUMBNAIL_BASE_URL + key + TRAILER_THUMBNAIL_END_URL)
                .into(view.trailerImage)

            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/watch?v=$key")
                intent.setPackage("com.google.android.youtube")
                context.startActivity(intent)
            })
        }
    }
}