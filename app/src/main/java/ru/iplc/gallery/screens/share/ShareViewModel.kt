package ru.iplc.gallery.screens.share

import android.net.Uri
import ru.iplc.gallery.common.SingleLiveEvent
import ru.iplc.gallery.data.FeedPostsRepository
import ru.iplc.gallery.data.UsersRepository
import ru.iplc.gallery.models.FeedPost
import ru.iplc.gallery.models.User
import ru.iplc.gallery.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Tasks

class ShareViewModel(private val feedPostsRepo: FeedPostsRepository,
                     private val usersRepo: UsersRepository,
                     onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener) {
    private val _shareCompletedEvent = SingleLiveEvent<Unit>()
    val shareCompletedEvent = _shareCompletedEvent
    val user = usersRepo.getUser()

    fun share(user: User, imageUri: Uri?, caption: String) {
        if (imageUri != null) {
            usersRepo.uploadUserImage(user.uid, imageUri).onSuccessTask { downloadUrl ->
                Tasks.whenAll(
                        usersRepo.setUserImage(user.uid, downloadUrl!!),
                        feedPostsRepo.createFeedPost(user.uid, mkFeedPost(user, caption,
                                downloadUrl.toString()))
                )
            }.addOnCompleteListener{
                _shareCompletedEvent.call()
            }.addOnFailureListener(onFailureListener)
        }
    }

    private fun mkFeedPost(user: User, caption: String, imageDownloadUrl: String): FeedPost {
        return FeedPost(
                uid = user.uid,
                username = user.username,
                image = imageDownloadUrl,
                caption = caption,
                photo = user.photo
        )
    }
}