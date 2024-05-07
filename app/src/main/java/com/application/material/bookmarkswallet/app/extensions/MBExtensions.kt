package com.application.material.bookmarkswallet.app.extensions

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.google.android.material.card.MaterialCardView

/**
 * extension fun
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun MaterialCardView.setStarColor(viewType: BookmarkFilter.StarFilterTypeEnum) {
    when (viewType) {
        IS_DEFAULT_VIEW -> {
            setBackgroundResource(R.drawable.bck_gradient_violet)
            setStrokeColorByColorRes(R.color.colorPrimaryDark)
        }

        IS_STAR_VIEW -> {
            setBackgroundResource(R.drawable.bck_gradient_yellow)
            setStrokeColorByColorRes(R.color.colorAccent)
        }
    }
}

fun MaterialCardView.setStarOutlineColor(viewType: BookmarkFilter.StarFilterTypeEnum) {
    when (viewType) {
        IS_DEFAULT_VIEW ->
            setStrokeColorByColorRes(R.color.colorPrimaryDark)

        IS_STAR_VIEW ->
            setStrokeColorByColorRes(R.color.colorAccent)
    }
}

fun View.toggleVisibiltyWithView(view: View) {
    visibility = View.GONE
    view.visibility = View.VISIBLE
}

fun MaterialCardView.setStrokeColorByColorRes(colorRes: Int) {
    strokeColor = ContextCompat.getColor(context, colorRes)
}

fun ImageView.setColor(colorRes: Int) {
    setColorFilter(ContextCompat.getColor(context, colorRes))
}

fun ImageView.setIconByResource(resource: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, resource))
}

fun ImageView.setIconDependingOnSortAscending(isSortAscending: Boolean) {
    when {
        isSortAscending -> this.rotation = 180.0f //setImageResource(R.drawable.ic_sort_amount_up)
        else -> this.rotation = 0.0f //R.drawable.ic_sort_amount_up)
    }
}

fun MenuItem.toggleSetIconTintListByRes(context: Context, colorRes: Int, menuItem: MenuItem) {
    setIconTintListByRes(context, colorRes)
    menuItem.setIconTintListByRes(context, android.R.color.white)
}

fun MenuItem.setIconTintListByRes(context: Context, colorRes: Int) {
    this.iconTintList = ContextCompat.getColorStateList(context, colorRes)
}

fun Toolbar.changeToolbarFont() {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView && view.text == title) {
            view.typeface = ResourcesCompat.getFont(view.context, R.font.yantramanav_light)
            break
        }
    }
}

fun TextView.setColorByRes(resource: Int) {
    setTextColor(ContextCompat.getColor(context, resource))
}

fun Activity.hideKeyboardIfNeeded() {
    window.currentFocus?.let {
        when (it) {
            is EditText -> it.hideKeyboard()
        }
    }
}

fun AppCompatImageView.setImageDrawableByRes(res: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, res))
}

fun View.toggleVisibilty(oldVisibility: Int) {
    visibility = when (oldVisibility) {
        View.VISIBLE -> View.GONE
        View.GONE -> View.VISIBLE
        else -> oldVisibility
    }
}
