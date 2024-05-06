package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_LIST
import com.application.material.bookmarkswallet.app.models.BookmarkHeader
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_HEADER_TYPE
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_VIEW_TYPE
import com.application.material.bookmarkswallet.app.extensions.setStarOutlineColor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView

class BookmarkListAdapter(
    private var items: List<Any>,
    private val bookmarkFilter: BookmarkFilter,
    private val onBookmarkItemClicked: (position: Int, bookmark: Bookmark) -> Unit,
    private val onBookmarkStarlicked: (position: Int, bookmark: Bookmark) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class BookmarkViewItemType { BOOKMARK_VIEW_TYPE, BOOKMARK_HEADER_TYPE }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BOOKMARK_VIEW_TYPE.ordinal -> {
                when (bookmarkFilter.listViewType) {
                    IS_LIST.ordinal -> BookmarkViewHolder(parent.inflate(R.layout.bookmark_list_view_item, false))
                    else -> BookmarkViewHolder(parent.inflate(R.layout.bookmark_card_view_item, false))
                }
            }
            else -> {
                BookmarkHeaderViewHolder(parent.inflate(R.layout.bookmark_header_item, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getSpanSizeByPosition(position)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].let { item ->
            when (holder) {
                //BookmarkViewHolder
                is BookmarkViewHolder -> {
                    //set backround color
                    (holder.itemView as MaterialCardView).setStarOutlineColor(bookmarkFilter.starFilterType)

                    //set items view holder
                    (item as Bookmark).let {
                        holder.apply {
                            bookmarkTitle.apply {
                                text = item.title?.let { if (it.isNullOrBlank()) EMPTY_BOOKMARK_LABEL else it }
                            }
                            bookmarkUrl.text = "https://${item.url}"
                            bookmarkTimestamp.text = item.timestamp?.toString() //"dd MMM"

                            bookmarkStarButton.apply {
                                setColorFilter(getColorByStarType(this.context, item.isStar))
                            }

                            itemView.setOnClickListener {
                                onBookmarkItemClicked(position, item)
                            }
                            bookmarkStarButton.setOnClickListener {
                                onBookmarkStarlicked(position, item)
                            }
                        }
                        //TODO handle how to move this
                        Glide.with(holder.itemView.context)
                            .load(item.image)
                            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
                            .placeholder(R.drawable.ic_bookmark)
                            .into(holder.bookmarkIcon)
                    }
                }
                //BookmarkHeaderViewHolder
                is BookmarkHeaderViewHolder -> {
                    (item as BookmarkHeader).let {
                        holder.apply {
                            bookmarkLabelHeader.text = item.label
                        }
                    }
                }
                else -> null
            }
        }
    }

    private fun getColorByStarType(context: Context, isStar: Boolean): Int = when (isStar) {
            true -> ContextCompat.getColor(context, R.color.colorAccent)
            else -> ContextCompat.getColor(context, R.color.colorPrimary)
        }

    /***
     * set items
     */
    fun setItems(list: List<Any>) {
        items = list
    }

    fun getSpanSizeByPosition(position: Int): Int {
        return when (items[position]) {
            is BookmarkHeader -> BOOKMARK_HEADER_TYPE.ordinal
            is Bookmark -> BOOKMARK_VIEW_TYPE.ordinal
            else -> BOOKMARK_VIEW_TYPE.ordinal
        }
    }

    /**
     * view holder
     */
    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkTitle: TextView = itemView.findViewById(R.id.bookmarkTitleTextViewId)
        val bookmarkUrl: TextView = itemView.findViewById(R.id.bookmarkUrlTextViewId)
        val bookmarkTimestamp: TextView = itemView.findViewById(R.id.bookmarkTimestampTextViewId)
        val bookmarkIcon: ImageView = itemView.findViewById(R.id.bookmarkIconImageViewId)
        val bookmarkStarButton: ImageView = itemView.findViewById(R.id.mbBookmarkStarButtonId)
    }
    /**
     * view holder
     */
    class BookmarkHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkSubtitleHeader: TextView = itemView.findViewById(R.id.bookmarkSubtitleHeaderViewId)
        val bookmarkLabelHeader: TextView = itemView.findViewById(R.id.bookmarkLabelHeaderViewId)

    }

    companion object {
        const val EMPTY_BOOKMARK_LABEL = "Title..."
    }

}

/***
 * add inflate function on ViewGroup item
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
