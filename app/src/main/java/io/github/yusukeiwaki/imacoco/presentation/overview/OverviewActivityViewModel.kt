package io.github.yusukeiwaki.imacoco.presentation.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import io.github.yusukeiwaki.imacoco.presentation.base.ProgressState
import io.github.yusukeiwaki.imacoco.repository.current_user.CurrentUserManager
import io.github.yusukeiwaki.imacoco.repository.device_registration.DeviceRegistrationManager
import io.github.yusukeiwaki.imacoco.repository.location_log.LocationLogManager

class OverviewActivityViewModel : ViewModel() {
    private val logoutState = MutableLiveData<ProgressState<Unit>?>()

    fun logoutState(): LiveData<ProgressState<Unit>?> = logoutState

    fun resetLogoutState() {
        logoutState.postValue(null)
    }

    fun logout() {
        CurrentUserManager().firebaseCurrentUser?.let { currentUser ->
            logoutState.postValue(ProgressState.ofProgress())
            val beforeLogout = Tasks.whenAllComplete(setOf<Task<Void>>(
                    DeviceRegistrationManager().unregister(currentUser.uid),
                    LocationLogManager().clear(currentUser.uid)))
            beforeLogout.addOnCompleteListener{
                CurrentUserManager().logout()
                logoutState.postValue(ProgressState.ofSuccess())
            }.addOnFailureListener { error ->
                logoutState.postValue(ProgressState.ofFailure(error))
            }
        }
    }
}
