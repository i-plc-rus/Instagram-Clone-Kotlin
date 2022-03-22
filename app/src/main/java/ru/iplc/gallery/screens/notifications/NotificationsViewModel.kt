package ru.iplc.gallery.screens.notifications

import androidx.lifecycle.LiveData
import ru.iplc.gallery.data.NotificationsRepository
import ru.iplc.gallery.models.Notification
import ru.iplc.gallery.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class NotificationsViewModel(private val notificationsRepo: NotificationsRepository,
                             onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener) {
    lateinit var notifications: LiveData<List<Notification>>
    private lateinit var uid: String

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            notifications = notificationsRepo.getNotifications(uid)
        }
    }

    fun setNotificationsRead(notifications: List<Notification>) {
        val ids = notifications.filter { !it.read }.map { it.id }
        if (ids.isNotEmpty()) {
            notificationsRepo.setNotificationsRead(uid, ids, true)
                    .addOnFailureListener(onFailureListener)
        }
    }

}