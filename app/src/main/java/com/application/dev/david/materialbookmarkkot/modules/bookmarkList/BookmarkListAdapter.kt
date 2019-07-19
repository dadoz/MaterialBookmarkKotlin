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
import khronos.toString

class BookmarkListAdapter(private val items: List<Bookmark>, val listener: OnBookmarkItemClickListener) : RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflatedView = parent.inflate(R.layout.bookmark_view_item, false)
        return BookmarkViewHolder(inflatedView, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bookmarkTitle.text = items[position].title
        holder.bookmarkUrl.text = items[position].url
        holder.bookmarkTimestamp.text = items[position].timestamp?.toString("dd MMM")
        Glide.with(holder.itemView.context)
            .load("https://instagram-brand.com/wp-content/uploads/2016/11/Instagram_AppIcon_Aug2017.png?w=300")
            .into(holder.bookmarkIcon)
    }

    /**
     * view holder
     */
    class BookmarkViewHolder(
        itemView: View,
        val listener: OnBookmarkItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val bookmarkTitle: TextView = itemView.findViewById(R.id.bookmarkTitleTextViewId)
        val bookmarkUrl: TextView = itemView.findViewById(R.id.bookmarkUrlTextViewId)
        val bookmarkTimestamp: TextView = itemView.findViewById(R.id.bookmarkTimestampTextViewId)
        val bookmarkIcon: ImageView = itemView.findViewById(R.id.bookmarkIconImageViewId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
            listener.onBookmarkItemClicked(v)
        }

    }

    interface OnBookmarkItemClickListener {
        fun onBookmarkItemClicked(view: View)
    }
}

/***
 * add inflate function on ViewGroup item
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
