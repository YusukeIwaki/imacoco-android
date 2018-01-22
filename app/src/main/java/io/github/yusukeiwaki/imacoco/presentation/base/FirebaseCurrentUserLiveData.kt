package io.github.yusukeiwaki.imacoco.presentation.base

import android.arch.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser

class FirebaseCurrentUserLiveData(private val firebaseAuth: FirebaseAuth) : LiveData<FirebaseUser>() {
    private var currentUser: FirebaseUser? = null
    private val firebaseAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        if (currentUser?.uid != firebaseAuth.currentUser?.uid) {
            currentUser = firebaseAuth.currentUser
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
