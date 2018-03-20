package io.github.yusukeiwaki.imacoco.repository.current_user

import android.arch.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase
import io.github.yusukeiwaki.imacoco.extension.map
import io.github.yusukeiwaki.imacoco.repository.base.FirebaseQueryLiveData
import io.github.yusukeiwaki.imacoco.repository.base.NullLiveData

object ShareUrlLiveData {
    private fun ref(uid: String) =
            FirebaseDatabase.getInstance().reference.child("users").child(uid).child("share_url").ref

    private fun newLiveData(uid: String): LiveData<String?> =
            FirebaseQueryLiveData(ref(uid)).map { snapshot -> snapshot?.getValue(String::class.java) }

    fun of(uid: String?) = uid?.let { newLiveData(it) } ?: NullLiveData<String>()
}
