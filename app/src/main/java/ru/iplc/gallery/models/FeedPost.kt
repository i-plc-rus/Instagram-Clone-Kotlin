package ru.iplc.gallery.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class FeedPost(val uid: String = "", val username: String = "",
                    val image: String = "", val caption: String = "",
                    val comments: List<Comment> = emptyList(),
                    val timestamp: Any = ServerValue.TIMESTAMP, val photo: String? = null,
                    @get:Exclude val id: String = "", @get:Exclude val commentsCount: Int = 0) {
    fun timestampDate(): Date = Date(timestamp as Long)
}