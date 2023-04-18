package com.example.hxh_project.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.hxh_project.R

class ProfileContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): LinearLayout(context, attrs, defStyleAttr) {

    private val userImage: ImageView
    private val userName: TextView
    private val jobTitle: TextView

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.profile_info, this, true)

        userImage = root.findViewById(R.id.ivProfile)
        userName = root.findViewById(R.id.tvProfileName)
        jobTitle = root.findViewById(R.id.tvJobTitle)
    }

    fun setImage(imgUrl: String) {
        userImage.load(imgUrl) {
            crossfade(true)
            placeholder(R.drawable.img_logo)
            error(R.drawable.img_logo)
            transformations(CircleCropTransformation())
        }
    }

    fun setUsername(username: String) {
        userName.text = username
    }

    fun setJobTitle(jobTitle: String) {
        this.jobTitle.text = jobTitle
    }
}