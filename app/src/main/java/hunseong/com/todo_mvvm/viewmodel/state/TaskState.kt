package hunseong.com.todo_mvvm.viewmodel.state

import androidx.annotation.StringRes
import hunseong.com.todo_mvvm.data.entity.TaskEntity

sealed class TaskState {

    object Uninitialized : TaskState()

    object Loading: TaskState()

    object EmptyCompletedTask : TaskState()

    data class Success(
        val tasks: List<TaskEntity>
    ): TaskState()

    data class Error(
        @StringRes val message: Int,
        val error: String
    ): TaskState()
}
