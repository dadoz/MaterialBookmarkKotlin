package com.application.material.bookmarkswallet.app.features.bookmarkList.adapter

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
import com.application.material.bookmarkswallet.app.extensions.setStarOutlineColor
import com.application.material.bookmarkswallet.app.features.bookmarkList.adapter.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_HEADER_TYPE
import com.application.material.bookmarkswallet.app.features.bookmarkList.adapter.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_VIEW_TYPE
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_LIST
import com.application.material.bookmarkswallet.app.models.BookmarkHeader
import com.application.material.bookmarkswallet.app.models.setImageViewSquaredResource
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import com.application.material.bookmarkswallet.app.utils.HTTPS_SCHEMA
import com.google.android.material.card.MaterialCardView
import timber.log.Timber

class BookmarkListAdapter(
    private var items: List<Any>,
    private val bookmarkFilter: BookmarkFilter,
    private val onBookmarkItemClicked: (position: Int, bookmark: Bookmark) -> Unit,
    private val onBookmarkStarClicked: (position: Int, bookmark: Bookmark) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class BookmarkViewItemType { BOOKMARK_VIEW_TYPE, BOOKMARK_HEADER_TYPE }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.adapter?.notifyDataSetChanged()
        Timber.e("RecyclerView- onAttachedToRecyclerView " + recyclerView.adapter?.itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BOOKMARK_VIEW_TYPE.ordinal -> {
                when (bookmarkFilter.listViewType) {
                    IS_LIST.ordinal -> BookmarkViewHolder(
                        parent.inflate(
                            R.layout.bookmark_list_view_item,
                            false
                        )
                    )

                    else -> BookmarkViewHolder(
                        parent.inflate(
                            R.layout.bookmark_card_view_item,
                            false
                        )
                    )
                }
            }

            else -> {
                BookmarkHeaderViewHolder(parent.inflate(R.layout.bookmark_header_item, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getSpanSizeByPosition(position)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].let { item ->
            when (holder) {
                //BookmarkViewHolder
                is BookmarkViewHolder -> onBindViewBookmarkVh(
                    holder = holder,
                    item = item as Bookmark
                )
                //BookmarkHeaderViewHolder
                is BookmarkHeaderViewHolder -> onBindViewHeaderVh(
                    holder = holder,
                    item = item as BookmarkHeader
                )

                else -> null
            }
        }
    }

    private fun onBindViewHeaderVh(holder: BookmarkHeaderViewHolder, item: BookmarkHeader) {
        holder.apply {
            bookmarkLabelHeader.text = item.label
        }
    }

    private fun onBindViewBookmarkVh(holder: BookmarkViewHolder, item: Bookmark) {
        //set backround color
        (holder.itemView as MaterialCardView).setStarOutlineColor(bookmarkFilter.starFilterType)

        //set items view holder
        holder.apply {
            bookmarkTitle.apply {
                text =
                    item.title
                        ?.ifBlank { EMPTY_BOOKMARK_LABEL }
            }
            bookmarkUrl.text = HTTPS_SCHEMA.plus(item.url)
            bookmarkTimestamp.text = item.timestamp?.toString() //"dd MMM"

            bookmarkStarButton.apply {
                setColorFilter(getColorByStarType(this.context, item.isLike))
            }

            itemView.setOnClickListener {
                onBookmarkItemClicked(position, item)
            }
            bookmarkStarButton.setOnClickListener {
                onBookmarkStarClicked(position, item)
            }
        }

        //set images
        setImageViewSquaredResource(
            holder.bookmarkIcon,
            item.iconUrl
        )
    }

    private fun getColorByStarType(context: Context, isStar: Boolean): Int = ContextCompat.getColor(
        context,
        when {
            isStar -> R.color.colorAccent
            else -> R.color.colorPrimary
        }
    )

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

    fun removeFromList(position: Int) {
        items = items.toMutableList()
            .also {
                it.removeAt(index = position)
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
        val bookmarkLabelHeader: TextView = itemView.findViewById(R.id.bookmarkLabelHeaderViewId)
    }
}

/***
 * add inflate function on ViewGroup item
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
