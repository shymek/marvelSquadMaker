package pl.jsm.marvelsquad.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.widget.Button
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Button.animateColorChange(@ColorRes from: Int, @ColorRes to: Int) {
    val colorFrom = ContextCompat.getColor(context, from)
    val colorTo = ContextCompat.getColor(context, to)

    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimator.addUpdateListener {
        backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
    }
    colorAnimator.start()
}

fun Button.setColorTint(@ColorRes color: Int) {
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
}