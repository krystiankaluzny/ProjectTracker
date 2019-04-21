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

    backgroundGradient(color, false, false)
}

@BindingAdapter("backgroundVerticalGradientBase")
fun View.bindBackgroundVerticalGradientBase(color: Int) {

    backgroundGradient(color, true, false)
}

@BindingAdapter("backgroundHorizontalGradientInvertBase")
fun View.bindBackgroundHorizontalGradientInvertBase(color: Int) {

    backgroundGradient(color, false, true)
}

@BindingAdapter("backgroundVerticalGradientInvertBase")
fun View.bindBackgroundVerticalGradientInvertBase(color: Int) {

    backgroundGradient(color, true, true)
}

private fun View.backgroundGradient(baseColor: Int, vertical: Boolean, invert: Boolean) {

    val lighterColor = ColorUtils.setAlphaComponent(baseColor, 100)
    val transparentColor = ColorUtils.setAlphaComponent(baseColor, 0)

    val shapeDrawable = ShapeDrawable(RectShape())

    val colors = if (invert) intArrayOf(transparentColor, lighterColor, baseColor)
                 else intArrayOf(baseColor, lighterColor, transparentColor)

    val positions = if (invert) floatArrayOf(0f, 0.4f, 1f) else floatArrayOf(0f, 0.6f, 1f)

    shapeDrawable.paint.shader = LinearGradient(
            0f, if (vertical) height.toFloat() else 0f,
            if (vertical) 0f else width.toFloat(), 0f,
            colors,
            positions,
            Shader.TileMode.CLAMP)

    background = shapeDrawable
}