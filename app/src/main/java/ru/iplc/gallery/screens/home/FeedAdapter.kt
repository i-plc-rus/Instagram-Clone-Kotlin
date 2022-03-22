package ru.iplc.gallery.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.FeedItemBinding
import ru.iplc.gallery.models.FeedPost
import ru.iplc.gallery.screens.common.SimpleCallback
import ru.iplc.gallery.screens.common.loadImage
import ru.iplc.gallery.screens.common.loadUserPhoto
import ru.iplc.gallery.screens.common.setCaptionText
//import kotlinx.android.synthetic.main.feed_item.view.*

class FeedAdapter(private val listener: Listener)
    : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private lateinit var binding: FeedItemBinding
    interface Listener {
        fun toggleLike(postId: String)
        fun loadLikes(postId: String, position: Int)
        fun openComments(postId: String)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var posts = listOf<FeedPost>()
    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()
    private val defaultPostLikes = FeedPostLikes(0, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        binding = FeedItemBinding.bind(view)
        return ViewHolder(view)
    }

    fun updatePostLikes(position: Int, likes: FeedPostLikes) {
        postLikes += (position to likes)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        val likes = postLikes[position] ?: defaultPostLikes
        with(holder.view) {
            binding.userPhotoImage.loadUserPhoto(post.photo)
            binding.usernameText.text = post.username
            binding.postImage.loadImage(post.image)
            if (likes.likesCount == 0) {
                binding.likesText.visibility = View.GONE
            } else {
                binding.likesText.visibility = View.VISIBLE
                val likesCountText = holder.view.context.resources.getQuantityString(
                        R.plurals.likes_count, likes.likesCount, likes.likesCount)
                binding.likesText.text = likesCountText
            }
            binding.captionText.setCaptionText(post.username, post.caption)
            binding.likeImage.setOnClickListener { listener.toggleLike(post.id) }
            binding.likeImage.setImageResource(
                    if (likes.likedByUser) R.drawable.ic_likes_active
                    else R.drawable.ic_likes_border)
            binding.commentImage.setOnClickListener { listener.openComments(post.id) }
            listener.loadLikes(post.id, position)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<FeedPost>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(this.posts, newPosts) { it.id })
        this.posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }
}