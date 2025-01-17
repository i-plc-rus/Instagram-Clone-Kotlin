package ru.iplc.gallery.data.firebase

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import ru.iplc.gallery.common.toUnit
import ru.iplc.gallery.data.SearchRepository
import ru.iplc.gallery.data.common.map
import ru.iplc.gallery.data.firebase.common.FirebaseLiveData
import ru.iplc.gallery.data.firebase.common.database
import ru.iplc.gallery.models.SearchPost
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

class FirebaseSearchRepository : SearchRepository {
    override fun createPost(post: SearchPost): Task<Unit> =
            database.child("search-posts").push()
                    .setValue(post.copy(caption = post.caption.toLowerCase())).toUnit()

    override fun searchPosts(text: String): LiveData<List<SearchPost>> {


        val reference = database.child("search-posts")
        val query = if (text.isEmpty()) {
            reference
        } else {
            reference.orderByChild("caption")
                    .startAt(text.toLowerCase()).endAt("${text.toLowerCase()}\\uf8ff")
        }
        //Log.d("ERROR_DB",FirebaseLiveData(query).value.toString())
        //return listOf <SearchPost>(null)
        var mydataSnapshot = FirebaseLiveData(query).map {
                        it.children.map { it.asSearchPost()!! }
                        }
        Log.d("ERROR_DB", listOf(mydataSnapshot)[0].toString())
        return mydataSnapshot
    }
}

private fun DataSnapshot.asSearchPost(): SearchPost? =
        getValue(SearchPost::class.java)?.copy(id = key!!)