package ru.iplc.gallery.screens.addfriends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityAddFriendsBinding
import ru.iplc.gallery.databinding.AddFriendsItemBinding
import ru.iplc.gallery.models.User
import ru.iplc.gallery.screens.common.SimpleCallback
import ru.iplc.gallery.screens.common.loadUserPhoto
//import kotlinx.android.synthetic.main.add_friends_item.view.*

class FriendsAdapter(private val listener: Listener)
    : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    private lateinit var binding: AddFriendsItemBinding
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface Listener {
        fun follow(uid: String)
        fun unfollow(uid: String)
    }

    private var mUsers = listOf<User>()
    private var mPositions = mapOf<String, Int>()
    private var mFollows = mapOf<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.add_friends_item, parent, false)
        binding = AddFriendsItemBinding.bind(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view) {
            val user = mUsers[position]
            binding.photoImage.loadUserPhoto(user.photo)
            binding.usernameText.text = user.username
            binding.nameText.text = user.name
            binding.followBtn.setOnClickListener { listener.follow(user.uid) }
            binding.unfollowBtn.setOnClickListener { listener.unfollow(user.uid) }

            val follows = mFollows[user.uid] ?: false
            if (follows) {
                binding.followBtn.visibility = View.GONE
                binding.unfollowBtn.visibility = View.VISIBLE
            } else {
                binding.followBtn.visibility = View.VISIBLE
                binding.unfollowBtn.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = mUsers.size

    fun update(users: List<User>, follows: Map<String, Boolean>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(mUsers, users) { it.uid })
        mUsers = users
        mPositions = users.withIndex().map { (idx, user) -> user.uid to idx }.toMap()
        mFollows = follows
        diffResult.dispatchUpdatesTo(this)
    }

    fun followed(uid: String) {
        mFollows += (uid to true)
        notifyItemChanged(mPositions[uid]!!)
    }

    fun unfollowed(uid: String) {
        mFollows -= uid
        notifyItemChanged(mPositions[uid]!!)
    }
}