package ru.iplc.gallery.screens.profile

import androidx.lifecycle.LiveData
import ru.iplc.gallery.data.UsersRepository
import ru.iplc.gallery.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener

class ProfileViewModel(private val usersRepo: UsersRepository, onFailureListener: OnFailureListener)
    : BaseViewModel(onFailureListener) {
    val user = usersRepo.getUser()
    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        if (!this::images.isInitialized) {
            images = usersRepo.getImages(uid)
        }
    }
}