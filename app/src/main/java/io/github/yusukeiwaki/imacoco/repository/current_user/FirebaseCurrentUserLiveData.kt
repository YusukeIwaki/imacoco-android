package io.github.yusukeiwaki.imacoco.repository.current_user

import android.arch.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser

class FirebaseCurrentUserLiveData : LiveData<FirebaseUser>() {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var currentUser: FirebaseUser? = null
    private val firebaseAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val newCurrentUser = firebaseAuth.currentUser
        if (currentUser?.uid != newCurrentUser?.uid) {
            currentUser = newCurrentUser
            notifyCurrentUser()
        }
    }

    override fun onActive() {
        super.onActive()
        currentUser = firebaseAuth.currentUser
        notifyCurrentUser()
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener)
        super.onInactive()
    }

    private fun notifyCurrentUser() {
        postValue(currentUser)
    }
}
