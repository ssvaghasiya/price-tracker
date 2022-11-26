package com.example.transoapp.extension

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.transoapp.R

/** Visibility Extension **/
fun View.viewGone() {
    visibility = View.GONE
}

fun View.viewShow() {
    visibility = View.VISIBLE
}

fun View.viewInvisible() {
    visibility = View.INVISIBLE
}

fun View.isViewVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun AppCompatImageView.loadImage(image: String?) {
    Glide.with(this.context)
        .load(image?.toUri()?.buildUpon()?.scheme("https")?.build())
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)
        .into(this)
}

