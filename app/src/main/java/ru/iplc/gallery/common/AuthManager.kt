package ru.iplc.gallery.common

import com.google.android.gms.tasks.Task

interface AuthManager {
    fun signOut()
    fun signIn(email: String, password: String): Task<Unit>
}