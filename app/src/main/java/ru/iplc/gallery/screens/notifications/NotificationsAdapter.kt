package ru.iplc.gallery.screens.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.NotificationItemBinding
import ru.iplc.gallery.models.Notification
import ru.iplc.gallery.models.NotificationType
import ru.iplc.gallery.screens.common.SimpleCallback
import ru.iplc.gallery.screens.common.loadImageOrHide
import ru.iplc.gallery.screens.common.loadUserPhoto
import ru.iplc.gallery.screens.common.setCaptionText
//import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var notifications = listOf<Notification>()
    private lateinit var binding: NotificationItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_item, parent, false)
        binding = NotificationItemBinding.bind(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        with(holder.view) {
            binding.userPhoto.loadUserPhoto(notification.photo)
            val notificationText = when (notification.type) {
                NotificationType.Comment -> context.getString(R.string.commented, notification.commentText)
                NotificationType.Like -> context.getString(R.string.liked_your_post)
                NotificationType.Follow -> context.getString(R.string.started_following_you)
            }
            binding.notificationText.setCaptionText(notification.username, notificationText,
                    notification.timestampDate())
            binding.postImage.loadImageOrHide(notification.postImage)
        }
    }

    override fun getItemCount(): Int = notifications.size

    fun updateNotifications(newNotifications: List<Notification>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(notifications, newNotifications) { it.id })
        this.notifications = newNotifications
        diffResult.dispatchUpdatesTo(this)
    }
}