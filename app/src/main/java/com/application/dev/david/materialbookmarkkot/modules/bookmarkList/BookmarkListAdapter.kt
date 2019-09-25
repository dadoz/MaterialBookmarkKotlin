package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import khronos.toString

class BookmarkListAdapter(private val items: List<Bookmark>, val listener: OnBookmarkItemClickListener) : RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflatedView = parent.inflate(R.layout.bookmark_view_item, false)
        return BookmarkViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bookmarkTitle.text = items[position].title
        holder.bookmarkUrl.text = items[position].url
        holder.bookmarkTimestamp.text = items[position].timestamp?.toString("dd MMM")
        Glide.with(holder.itemView.context)
            .load(items[position].image)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
            .into(holder.bookmarkIcon)

        holder.itemView.setOnClickListener { listener.onBookmarkItemClicked(position, items[position]) }
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

    interface OnBookmarkItemClickListener {
        fun onBookmarkItemClicked(position: Int, bookmark: Bookmark)
    }
}

/***
 * add inflate function on ViewGroup item
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
