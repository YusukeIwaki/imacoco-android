package io.github.yusukeiwaki.imacoco.repository.base

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

/**
 * ref: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */
class FirebaseQueryLiveData(private val query: Query) : LiveData<DataSnapshot?>() {
    companion object {
        private val LOG_TAG = "FirebaseQueryLiveData"
    }

    private val listener = MyValueEventListener()

    override fun onActive() {
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot?) {
            postValue(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(LOG_TAG, "Can't listen to query $query", databaseError.toException())
        }
    }
}