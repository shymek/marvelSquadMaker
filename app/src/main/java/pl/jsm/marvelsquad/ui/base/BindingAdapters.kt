package pl.jsm.marvelsquad.ui.base

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import pl.jsm.marvelsquad.R
import pl.jsm.marvelsquad.ui.GlideApp
import pl.jsm.marvelsquad.utils.animateColorChange
import pl.jsm.marvelsquad.utils.setColorTint

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("isRefreshing")
    fun isRefreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
        view.isRefreshing = refreshing
    }

    @JvmStatic
    @BindingAdapter("characterAvatar")
    fun characterAvatar(view: ImageView, email: String?) {
        email?.let {
            GlideApp.with(view).clear(view)
            GlideApp.with(view)
                .load(it)
                .circleCrop()
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view).clear(view)
            Glide.with(view)
                .load(it)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("visibleIf")
    fun visibleIf(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("titleId")
    fun setTitleId(toolbar: Toolbar, @StringRes titleId: Int) {
        toolbar.setTitle(titleId)
    }

    @JvmStatic
    @BindingAdapter("titleValue")
    fun setTitle(toolbar: Toolbar, title: String) {
        toolbar.title = title
    }

    @JvmStatic
    @BindingAdapter("comicCount")
    fun setComicCount(view: TextView, count: Int) {
        view.text =
            view.context.resources.getQuantityString(R.plurals.label_other_comics, count, count)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["isHired", "shouldAnimateColorChange"],
        requireAll = false
    )
    fun setIsHired(button: Button, isHired: Boolean, shouldAnimateColorChange: Boolean) {
        val context = button.context
        val currentColorId = if (isHired) R.color.button_hire else R.color.button_fire
        val newColorId = if (isHired) R.color.button_fire else R.color.button_hire
        val textId = if (isHired) R.string.button_fire else R.string.button_hire
        val textToSet = context.getString(textId)

        button.apply {
            text = textToSet
            if (shouldAnimateColorChange) {
                animateColorChange(currentColorId, newColorId)
            } else {
                setColorTint(newColorId)
            }
        }
    }
}