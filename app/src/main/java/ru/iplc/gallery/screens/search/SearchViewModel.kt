package ru.iplc.gallery.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.iplc.gallery.data.SearchRepository
import ru.iplc.gallery.models.SearchPost
import ru.iplc.gallery.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class SearchViewModel(searchRepo: SearchRepository,
                      onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener) {
    private val searchText = MutableLiveData<String>()

    val posts: LiveData<List<SearchPost>> = Transformations.switchMap(searchText) { text ->
        searchRepo.searchPosts(text)
    }

    fun setSearchText(text: String) {
        searchText.value = text
    }
}