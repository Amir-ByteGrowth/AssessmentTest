package com.example.assessmenttest.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.assessmenttest.R
import com.example.assessmenttest.models.Comments

class CommentsAdaptor() : RecyclerView.Adapter<CommentsAdaptor.ViewHolder>() {


    private lateinit var commentsList: List<Comments>

//    lateinit var onItemClick: ViewHolder.onItemClick
    fun updatePostItems(postList: List<Comments>) {
        this.commentsList = postList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdaptor.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.comments_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsAdaptor.ViewHolder, position: Int) {
        holder.tvId.text = commentsList[position].id.toString()
        holder.tvName.setText(commentsList.get(position).name)
        holder.tvEmail.setText(commentsList.get(position).email)
        holder.tvBody.setText(commentsList.get(position).body)
//        holder.cardPostItem.setOnClickListener(View.OnClickListener {
//            onItemClick.onPostClick(position)
//        })
    }

    override fun getItemCount(): Int {
        return if (::commentsList.isInitialized) commentsList.size else 0
    }

    fun updatePostList(postList: List<Comments>) {
        this.commentsList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        var tvId: TextView = view.findViewById<View>(R.id.tvID) as TextView
        var tvName: TextView = view.findViewById<View>(R.id.tvName) as TextView
        var tvEmail: TextView = view.findViewById<View>(R.id.tvEmail) as TextView
        var tvBody: TextView = view.findViewById<View>(R.id.tvBody) as TextView
//        var cardPostItem = view.findViewById(R.id.cardPostItem) as CardView
//
//        interface onItemClick {
//            fun onPostClick(position: Int)
//        }
    }
}