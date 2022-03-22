package ru.iplc.gallery.common.firebase

import ru.iplc.gallery.common.AuthManager
import ru.iplc.gallery.common.toUnit
import ru.iplc.gallery.data.firebase.common.auth
import com.google.android.gms.tasks.Task

class FirebaseAuthManager : AuthManager {
    override fun signOut() {
        auth.signOut()
    }

    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()
}