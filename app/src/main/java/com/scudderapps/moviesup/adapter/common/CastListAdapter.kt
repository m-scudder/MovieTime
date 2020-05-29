package com.scudderapps.moviesup.adapter.common

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
import com.scudderapps.moviesup.CastAndCrewDetailActivity
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.models.common.CastDetail
import kotlinx.android.synthetic.main.cast_list_item.view.*
import android.util.Pair as UtilPair

class CastListAdapter(private val cast: ArrayList<CastDetail>, private val context: Context) :
    RecyclerView.Adapter<CastListAdapter.CastHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.cast_list_item, parent, false)
        return CastHolder(
            view
        )
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
            itemView.castName.text = cast.name
            itemView.characterName.text = cast.character
            if (!cast.profilePath.isNullOrEmpty()) {
                val profileUrl = IMAGE_BASE_URL + cast?.profilePath
                Glide.with(view)
                    .load(profileUrl)
                    .into(itemView.castImage)
            } else {
                Glide.with(view)
                    .load(R.drawable.default_avatar)
                    .into(itemView.castImage)
            }

            itemView.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, CastAndCrewDetailActivity::class.java)
                intent.putExtra("id", cast.id)
                Log.d("personId", cast.id.toString())
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    UtilPair<View, String>(itemView.castImage, "peopleImageTransition")
                )
                context.startActivity(intent, options.toBundle())
            })
        }
    }
}