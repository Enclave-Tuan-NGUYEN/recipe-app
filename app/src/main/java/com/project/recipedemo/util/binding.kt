package com.project.recipedemo.util

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.project.recipedemo.R

@BindingAdapter("android:srcImage")
fun setSrcImage(view: ImageView, path: String) {
    val context = view.context
    Glide.with(context).load(path).into(view)
}

@BindingAdapter("android:textBtn")
fun setText(view: Button, isEditable: Boolean) {
    val  context = view.context
    if (isEditable) {
        view.text = context.getString(R.string.label_btn_edit)
    } else {
        view.text = context.getString(R.string.label_btn_add)
    }
}
