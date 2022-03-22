package ru.iplc.gallery.screens.home

import androidx.lifecycle.LiveData
import ru.iplc.gallery.common.SingleLiveEvent
import ru.iplc.gallery.data.FeedPostsRepository
import ru.iplc.gallery.data.common.map
import ru.iplc.gallery.models.FeedPost
import ru.iplc.gallery.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class HomeViewModel(onFailureListener: OnFailureListener,
                    private val feedPostsRepo: FeedPostsRepository) : BaseViewModel(onFailureListener) {
    lateinit var uid: String
    lateinit var feedPosts: LiveData<List<FeedPost>>
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()
    private val _goToCommentsScreen = SingleLiveEvent<String>()
    val goToCommentsScreen = _goToCommentsScreen

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            feedPosts = feedPostsRepo.getFeedPosts(uid).map {
                it.sortedByDescending { it.timestampDate() }
            }
        }
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, uid).addOnFailureListener(onFailureListener)
    }

    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val existingLoadedLikes = loadedLikes[postId]
        if (existingLoadedLikes == null) {
            val liveData = feedPostsRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                        likesCount = likes.size,
                        likedByUser = likes.find { it.userId == uid } != null)
            }
            loadedLikes += postId to liveData
            return liveData
        } else {
            return existingLoadedLikes
        }
    }

    fun openComments(postId: String) {
        _goToCommentsScreen.value = postId
    }
}