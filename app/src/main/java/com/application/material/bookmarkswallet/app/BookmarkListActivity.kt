package com.application.material.bookmarkswallet.app

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.utils.FontSpan
import com.google.android.material.card.MaterialCardView

class BookmarkListActivity : AppCompatActivity(), OnFragmentInteractionListener, LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host = NavHostFragment.create(R.navigation.kmb_main_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, host).setPrimaryNavigationFragment(host).commit()
    }


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

/**
 * extension fun
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
}

fun View.setBackgroundColorByColorRes(colorRes: Int) {
    setBackgroundColor(ContextCompat.getColor(context, colorRes))
}

fun MaterialCardView.setBackgroundColorByColorRes(colorRes: Int) {
    setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
}

fun MaterialCardView.setStarColor(viewType: BookmarkFilter.StarFilterTypeEnum) {
    when (viewType) {
        IS_DEFAULT_VIEW -> {
//            setBackgroundColorByColorRes(R.color.colorPrimary)
            setBackgroundResource(R.drawable.bck_gradient_violet)
            setStrokeColorByColorRes(R.color.colorPrimaryDark)
        }
        IS_STAR_VIEW -> {
            setBackgroundResource(R.drawable.bck_gradient_yellow)
//            setBackgroundColorByColorRes(R.color.colorAccent)
            setStrokeColorByColorRes(R.color.colorAccent)
        }
    }
}

fun MaterialCardView.setStarOutlineColor(viewType: BookmarkFilter.StarFilterTypeEnum) {
    when (viewType) {
        IS_DEFAULT_VIEW -> {
            setBackgroundColorByColorRes(R.color.colorPrimaryLight)
            setStrokeColorByColorRes(R.color.colorPrimaryDark)
        }
        IS_STAR_VIEW -> {
            setBackgroundColorByColorRes(R.color.colorAccentLight)
            setStrokeColorByColorRes(R.color.colorAccent)
        }
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
    when (isSortAscending) {
        true -> setImageResource(R.drawable.ic_reorder_up)
        false -> setImageResource(R.drawable.ic_reorder_down)
    }
}

fun MenuItem.toggleSetIconTintListByRes(context: Context, colorRes: Int, menuItem: MenuItem) {
    setIconTintListByRes(context, colorRes)
    menuItem.setIconTintListByRes(context, android.R.color.white)
}

fun MenuItem.setIconTintListByRes(context: Context, colorRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
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
        VISIBLE -> GONE
        GONE -> VISIBLE
        else -> oldVisibility
    }
}

//fun MenuItem.applyFontToMenuItem(context: Context) {
//    SpannableString(title).apply {
//        setSpan(
//            FontSpan(ResourcesCompat.getFont(context, R.font.lato_light)),
//            0,
//            title?.length ?: 0,
//            Spannable.SPAN_INCLUSIVE_INCLUSIVE
//        )
//        title = this
//    }
//}
