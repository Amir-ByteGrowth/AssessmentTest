package com.example.assessmenttest.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.assessmenttest.R
import com.example.assessmenttest.models.FavPosts
import com.example.assessmenttest.models.Posts

class FavPostAdaptor() : RecyclerView.Adapter<FavPostAdaptor.ViewHolder>() {


    private lateinit var postList: List<FavPosts>

    lateinit var onItemClick: ViewHolder.onItemClick
    fun updatePostItems(postList: List<FavPosts>, onItemClick: ViewHolder.onItemClick) {
        this.postList = postList
        this.onItemClick = onItemClick
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavPostAdaptor.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavPostAdaptor.ViewHolder, position: Int) {
        holder.tvId.text = postList[position].id.toString()
        holder.tvTitle.setText(postList.get(position).title)
        holder.tvBody.setText(postList.get(position).body)
        holder.cardPostItem.setOnClickListener(View.OnClickListener {
            onItemClick.onPostClick(position)
        })
    }

    override fun getItemCount(): Int {
        return if (::postList.isInitialized) postList.size else 0
    }

    fun updatePostList(postList: List<FavPosts>) {
        this.postList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        var tvId: TextView = view.findViewById<View>(R.id.tvID) as TextView
        var tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView
        var tvBody: TextView = view.findViewById<View>(R.id.tvBody) as TextView
        var cardPostItem = view.findViewById(R.id.cardPostItem) as CardView

        interface onItemClick {
            fun onPostClick(position: Int)
        }
    }
}