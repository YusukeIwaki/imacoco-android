package io.github.yusukeiwaki.imacoco.presentation.base

class ProgressState<TResult> private constructor(val state: State, val result: TResult?, val error: Exception?) {
    enum class State {
        IN_PROGRESS,
        SUCCESS,
        FAILURE
    }

    companion object {
        fun <T> ofProgress() = ProgressState(State.IN_PROGRESS, null as T?, null)

        fun <T> ofSuccess(result: T) = ProgressState(State.SUCCESS, result, null)

        fun ofSuccess() = ProgressState<Unit>(State.SUCCESS, Unit, null)

        fun <T> ofFailure(error: Exception) = ProgressState(State.FAILURE, null as T?, error)
    }

    val isInProgress
        get() = state == State.IN_PROGRESS

    val isSuccess
        get() = state == State.SUCCESS && result != null

    val isError
        get() = state == State.FAILURE && error != null
}
