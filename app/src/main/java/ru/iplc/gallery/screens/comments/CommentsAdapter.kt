package ru.iplc.gallery.screens.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityCommentsBinding
import ru.iplc.gallery.databinding.CommentsItemBinding
import ru.iplc.gallery.models.Comment
import ru.iplc.gallery.screens.common.SimpleCallback
import ru.iplc.gallery.screens.common.loadUserPhoto
import ru.iplc.gallery.screens.common.setCaptionText
//import kotlinx.android.synthetic.main.comments_item.view.*

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var comments = listOf<Comment>()
    private lateinit var binding: CommentsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
               .inflate(R.layout.comments_item, parent, false)
        binding = CommentsItemBinding.bind(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        with(holder.view) {
            binding.photo.loadUserPhoto(comment.photo)
            binding.text.setCaptionText(comment.username, comment.text, comment.timestampDate())
        }
    }

    fun updateComments(newComments: List<Comment>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(comments, newComments) {it.id})
        this.comments = newComments
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = comments.size
}