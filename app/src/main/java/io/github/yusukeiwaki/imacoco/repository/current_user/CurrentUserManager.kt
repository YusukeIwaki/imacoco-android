package io.github.yusukeiwaki.imacoco.repository.current_user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CurrentUserManager {
    val firebaseCurrentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    fun login() = FirebaseAuth.getInstance().signInAnonymously()

    fun logout() = FirebaseAuth.getInstance().signOut()

    fun firebaseCurrentUserAsLiveData() = FirebaseCurrentUserLiveData()
}
