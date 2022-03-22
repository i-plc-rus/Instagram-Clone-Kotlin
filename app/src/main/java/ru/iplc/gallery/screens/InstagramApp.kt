package ru.iplc.gallery.screens

import android.app.Application
import ru.iplc.gallery.common.firebase.FirebaseAuthManager
import ru.iplc.gallery.data.firebase.FirebaseFeedPostsRepository
import ru.iplc.gallery.data.firebase.FirebaseNotificationsRepository
import ru.iplc.gallery.data.firebase.FirebaseSearchRepository
import ru.iplc.gallery.data.firebase.FirebaseUsersRepository
import ru.iplc.gallery.screens.notifications.NotificationsCreator
import ru.iplc.gallery.screens.search.SearchPostsCreator

class InstagramApp : Application() {
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val authManager by lazy { FirebaseAuthManager() }
    val searchRepo by lazy { FirebaseSearchRepository() }

    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
        SearchPostsCreator(searchRepo)
    }
}