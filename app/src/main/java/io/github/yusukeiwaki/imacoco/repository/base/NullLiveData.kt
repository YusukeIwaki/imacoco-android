package io.github.yusukeiwaki.imacoco.repository.base

import android.arch.lifecycle.LiveData

class NullLiveData<T> : LiveData<T?>() {
    init {
        postValue(null)
    }
}
