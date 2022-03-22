package ru.iplc.gallery.screens.addfriends

import androidx.lifecycle.LiveData
import ru.iplc.gallery.data.FeedPostsRepository
import ru.iplc.gallery.data.UsersRepository
import ru.iplc.gallery.data.common.map
import ru.iplc.gallery.models.User
import ru.iplc.gallery.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class AddFriendsViewModel(onFailureListener: OnFailureListener,
                          private val usersRepo: UsersRepository,
                          private val feedPostsRepo: FeedPostsRepository)
    : BaseViewModel(onFailureListener) {
    val userAndFriends: LiveData<Pair<User, List<User>>> =
            usersRepo.getUsers().map { allUsers ->
                val (userList, otherUsersList) = allUsers.partition {
                    it.uid == usersRepo.currentUid()
                }
                userList.first() to otherUsersList
            }

    fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {
        return (if (follow) {
            Tasks.whenAll(
                    usersRepo.addFollow(currentUid, uid),
                    usersRepo.addFollower(currentUid, uid),
                    feedPostsRepo.copyFeedPosts(postsAuthorUid = uid, uid = currentUid))
        } else {
            Tasks.whenAll(
                    usersRepo.deleteFollow(currentUid, uid),
                    usersRepo.deleteFollower(currentUid, uid),
                    feedPostsRepo.deleteFeedPosts(postsAuthorUid = uid, uid = currentUid))
        }).addOnFailureListener(onFailureListener)
    }
}