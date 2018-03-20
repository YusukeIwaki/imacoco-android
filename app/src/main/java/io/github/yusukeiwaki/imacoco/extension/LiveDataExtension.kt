package io.github.yusukeiwaki.imacoco.extension

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

fun <T, X> LiveData<T?>.map(function: (T?) -> X?): LiveData<X?> =
        Transformations.map(this, function)

fun <T, X> LiveData<T?>.switchMap(function: (T?) -> LiveData<X?>): LiveData<X?> =
        Transformations.switchMap(this, function)
