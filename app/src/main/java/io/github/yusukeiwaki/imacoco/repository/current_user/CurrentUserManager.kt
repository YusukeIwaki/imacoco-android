package io.github.yusukeiwaki.imacoco.repository.current_user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.yusukeiwaki.imacoco.extension.switchMap

class CurrentUserManager {
    val firebaseCurrentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    fun login() = FirebaseAuth.getInstance().signInAnonymously()

    fun logout() = FirebaseAuth.getInstance().signOut()

    fun firebaseCurrentUserAsLiveData() = FirebaseCurrentUserLiveData()

    fun shareUrlAsLiveData() =
            FirebaseCurrentUserLiveData().switchMap { firebaseUser -> ShareUrlLiveData.of(firebaseUser?.uid) }
}
