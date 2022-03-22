package ru.iplc.gallery.screens.search

import android.util.Log
import androidx.lifecycle.Observer
import ru.iplc.gallery.common.BaseEventListener
import ru.iplc.gallery.common.Event
import ru.iplc.gallery.common.EventBus
import ru.iplc.gallery.data.SearchRepository
import ru.iplc.gallery.models.SearchPost

class SearchPostsCreator(searchRepo: SearchRepository) : BaseEventListener() {
    init {
        EventBus.events.observe(this, Observer {
            it?.let { event ->
                when (event) {
                    is Event.CreateFeedPost -> {
                        val searchPost = with(event.post) {
                            SearchPost(
                                    image = image,
                                    caption = caption,
                                    postId = id)
                        }
                        searchRepo.createPost(searchPost).addOnFailureListener {
                            Log.d(TAG, "Failed to create search post for event: $event", it)
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "SearchPostsCreator"
    }
}