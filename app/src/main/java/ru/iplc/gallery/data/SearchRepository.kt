package ru.iplc.gallery.data

import androidx.lifecycle.LiveData
import ru.iplc.gallery.models.SearchPost
import com.google.android.gms.tasks.Task

interface SearchRepository {
    fun searchPosts(text: String): LiveData<List<SearchPost>>
    fun createPost(post: SearchPost): Task<Unit>
}