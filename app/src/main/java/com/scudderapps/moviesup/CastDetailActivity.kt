package com.scudderapps.moviesup

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide

class CastDetailActivity : AppCompatActivity() {

    @BindView(R.id.cast_profile_name)
    lateinit var castName: TextView

    @BindView(R.id.cast_profile_image)
    lateinit var castImage: ImageView

    @BindView(R.id.cast_profile_character)
    lateinit var castCharacter: TextView

    @BindView(R.id.cast_toolbar)
    lateinit var castToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cast_detail_activity)
        ButterKnife.bind(this)

        setSupportActionBar(castToolbar)
        supportActionBar!!.title = "Cast Details"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val castDetails = intent.extras
        var Name = castDetails!!.getString("name")
        var Profile = castDetails!!.getString("profileUrl")
        var Character = castDetails!!.getString("characterName")

        castName.text = Name
        castCharacter.text = Character
        Glide.with(this)
            .load(Profile)
            .placeholder(R.drawable.default_avatar)
            .into(castImage)
    }
}
