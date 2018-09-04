package org.projecttracker.view

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.support.v4.graphics.ColorUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun <T : ViewDataBinding> ViewGroup.bind(layoutRes: Int): T {

    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, false)
}

@BindingAdapter("visibleOrGone")
fun View.bindVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

@BindingAdapter("backgroundHorizontalGradientBase")
fun View.bindBackgroundHorizontalGradientBase(color: Int) {

    backgroundGradient(color, false)
}

@BindingAdapter("backgroundVerticalGradientBase")
fun View.bindBackgroundVerticalGradientBase(color: Int) {

    backgroundGradient(color, true)
}

private fun View.backgroundGradient(baseColor: Int, vertical: Boolean = false) {

    val lighterColor = ColorUtils.setAlphaComponent(baseColor, 100)
    val transparentColor = ColorUtils.setAlphaComponent(baseColor, 0)

    val shapeDrawable = ShapeDrawable(RectShape())

    shapeDrawable.paint.shader = LinearGradient(
        0f, if (vertical) height.toFloat() else 0f,
        if (vertical) 0f else width.toFloat(), 0f,
        intArrayOf(baseColor, lighterColor, transparentColor),
        floatArrayOf(0f, 0.6f, 1f),
        Shader.TileMode.CLAMP)

    background = shapeDrawable
}