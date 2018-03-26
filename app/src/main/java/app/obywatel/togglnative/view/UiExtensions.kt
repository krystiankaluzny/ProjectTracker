package app.obywatel.togglnative.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun <T : ViewDataBinding> ViewGroup.bind(layoutRes: Int): T {

    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, false)
}