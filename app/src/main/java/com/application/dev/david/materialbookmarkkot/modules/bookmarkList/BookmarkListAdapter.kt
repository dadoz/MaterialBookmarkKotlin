package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkHeader
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_HEADER_TYPE
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_VIEW_TYPE
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import khronos.toString

class BookmarkListAdapter(private var items: MutableList<Any>, private val onBookmarkItemClicked: (position: Int, bookmark: Bookmark) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EMPTY_BOOKMARK_LABEL = "Title..."
    public enum class BookmarkViewItemType { BOOKMARK_VIEW_TYPE, BOOKMARK_HEADER_TYPE }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BOOKMARK_VIEW_TYPE.ordinal -> {
                val inflatedView = parent.inflate(R.layout.bookmark_view_item, false)
                BookmarkViewHolder(inflatedView)
            }
            else -> {
                val inflatedView = parent.inflate(R.layout.bookmark_header_item, false)
                BookmarkHeaderViewHolder(inflatedView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is BookmarkHeader -> BOOKMARK_HEADER_TYPE.ordinal
            is Bookmark -> BOOKMARK_VIEW_TYPE.ordinal
            else -> BOOKMARK_VIEW_TYPE.ordinal
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].let { item ->
            when (holder) {
                //BookmarkViewHolder
                is BookmarkViewHolder -> {
                    (item as Bookmark).let {
                        holder.apply {
                            bookmarkTitle.text = item.title
                            bookmarkTitle.text?.let { EMPTY_BOOKMARK_LABEL }

                            bookmarkUrl.text = "https://${item.url}"
                            bookmarkTimestamp.text = item.timestamp?.toString("dd MMM")
                            itemView.setOnClickListener {
                                onBookmarkItemClicked(position, item)
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

    /***
     * set items
     */
    fun setItems(list: MutableList<Any>) {
        items = list
    }

    /**
     * view holder
     */
    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkTitle: TextView = itemView.findViewById(R.id.bookmarkTitleTextViewId)
        val bookmarkUrl: TextView = itemView.findViewById(R.id.bookmarkUrlTextViewId)
        val bookmarkTimestamp: TextView = itemView.findViewById(R.id.bookmarkTimestampTextViewId)
        val bookmarkIcon: ImageView = itemView.findViewById(R.id.bookmarkIconImageViewId)
    }
    /**
     * view holder
     */
    class BookmarkHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkSubtitleHeader: TextView = itemView.findViewById(R.id.bookmarkSubtitleHeaderViewId)
        val bookmarkLabelHeader: TextView = itemView.findViewById(R.id.bookmarkLabelHeaderViewId)

    }

}

/***
 * add inflate function on ViewGroup item
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
