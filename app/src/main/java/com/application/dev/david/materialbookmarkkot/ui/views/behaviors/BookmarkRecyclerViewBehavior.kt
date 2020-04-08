package com.application.dev.david.materialbookmarkkot.ui.views.behaviors

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.View.MeasureSpec.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter


class BookmarkRecyclerViewBehavior {
}

class BookmarkAnimator {
    /**
     * alpha animator
     */
    fun alphaAnimator(view: View, finalValue: Float): Animator {
        val initValue = if (finalValue == 0f) 1f else 0f
        return ObjectAnimator.ofFloat(view, "alpha", initValue, finalValue)
    }

    /**
     * expand animator
     */
    fun expandAnimator(view: View): Animator {
        view.measure(makeMeasureSpec((view.parent as View).width, EXACTLY),
            makeMeasureSpec(0, UNSPECIFIED))
        val targetHeight = view.measuredHeight

        return ValueAnimator.ofFloat(0F, 1F). apply {
            addUpdateListener {
                view.apply {
                    visibility = View.VISIBLE
                    layoutParams.height = when (it.animatedValue) {
                        1f -> CoordinatorLayout.LayoutParams.WRAP_CONTENT
                        else -> (targetHeight * it.animatedValue as Float).toInt()
                    }
                    requestLayout()
                }
            }
        }
    }

    /**
     * collapse animator
     */
    fun collapseAnimator(view: View): Animator {
        val initialHeight = view.measuredHeight
        return ValueAnimator.ofFloat(0F, 1F). apply {
            addUpdateListener {
                view.apply {
                    when (it.animatedValue) {
                        1f -> View.GONE
                        else -> {
                            view.layoutParams.height =
                                initialHeight - (initialHeight * it.animatedValue as Float).toInt()
                            view.requestLayout()

                        }
                    }
                }
            }
        }
    }
}

/***
 * extension class :P
 */
fun RecyclerView.addOnScrollListenerWithViews(views: List<View>) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var isAnimating = false
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollVertically(-1) &&
                newState == RecyclerView.SCROLL_STATE_IDLE) {
                isAnimating = false
                AnimatorSet().apply {
//                    duration = 500
                    BookmarkAnimator().apply {
                        playTogether(
                            expandAnimator(views[0]),
                            alphaAnimator(views[1], 1f),
                            alphaAnimator(views[2], 1f),
                            alphaAnimator(views[3], 1f)
                        )
                    }
                    start()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 10 && !isAnimating) {
                isAnimating = true
                AnimatorSet().apply {
//                    duration = 500
                    BookmarkAnimator().apply {
                        playTogether(
                            collapseAnimator(views[0]),
                            alphaAnimator(views[1], 0f),
                            alphaAnimator(views[2], 0f),
                            alphaAnimator(views[3], 0f)
                            )
                    }
                    start()
                }
            }
        }

    })
}

fun RecyclerView.setGridOrListLayout(listViewType: BookmarkFilter.ListViewTypeEnum,
                                     newSpanCount: Int) {
    this.apply {
        (layoutManager as GridLayoutManager).apply {
            this.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when ((adapter as BookmarkListAdapter).getSpanSizeByPosition(position)) {
                        BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_HEADER_TYPE.ordinal -> when(listViewType)  {
                            BookmarkFilter.ListViewTypeEnum.IS_LIST -> 1
                            BookmarkFilter.ListViewTypeEnum.IS_GRID -> 2
                        }
                        else -> 1
                    }
                }
            }
            //set span
            this.spanCount = newSpanCount
        }
        adapter?.notifyDataSetChanged()
    }
}