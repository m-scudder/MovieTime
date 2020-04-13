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
import com.scudderapps.moviesup.CastDetailActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
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
            val profileUrl = IMAGE_BASE_URL + cast?.profilePath
            Glide.with(view)
                .load(profileUrl)
                .placeholder(R.drawable.default_avatar)
                .into(itemView.cast_image)

            itemView.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, CastDetailActivity::class.java)
                intent.putExtra("name", cast?.name)
                intent.putExtra("profileUrl", profileUrl)
                intent.putExtra("characterName", cast?.character)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    Pair<View, String>(itemView.cast_image, "castImageTransition"),
                    Pair<View, String>(itemView.cast_name, "castNameTransition")
                )
                context.startActivity(intent, options.toBundle())
            })
        }
    }
}