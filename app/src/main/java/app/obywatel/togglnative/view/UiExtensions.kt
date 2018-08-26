package app.obywatel.togglnative.view

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
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

@BindingAdapter("backgroundGradientBase")
fun View.bindBackgroundGradientBase(color: Int) {

    val shapeDrawable = ShapeDrawable(RectShape())
    shapeDrawable.paint.shader = LinearGradient(0f, 0f, width.toFloat(), 0f,
        intArrayOf(color, Color.TRANSPARENT, Color.TRANSPARENT),
        floatArrayOf(0f, 0.9f, 1f),
        Shader.TileMode.REPEAT)

    background = shapeDrawable
}