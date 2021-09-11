package hunseong.com.todo_mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hunseong.com.todo_mvvm.R
import hunseong.com.todo_mvvm.data.entity.TaskEntity
import hunseong.com.todo_mvvm.data.repository.TaskRepository
import hunseong.com.todo_mvvm.viewmodel.state.TaskState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val taskRepository: TaskRepository,
) : BaseViewModel() {

    private val _taskLiveData = MutableLiveData<TaskState>(TaskState.Uninitialized)
    val taskLiveData: LiveData<TaskState>
        get() = _taskLiveData

    override fun fetchTasks(): Job = viewModelScope.launch {

        _taskLiveData.value = TaskState.Loading

        try {
            val tasks = taskRepository.getAllTasks()
                .sortedByDescending { it.id }
                .sortedBy { it.isCompleted }
            if(tasks.isNullOrEmpty()) {
                _taskLiveData.value = TaskState.Success(emptyList())
            } else {
                _taskLiveData.value = TaskState.Success(tasks)
            }
        } catch (e:Exception) {
            _taskLiveData.value = TaskState.Error(R.string.error_message, e.toString())
        }
    }

    fun insertTask(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insert(taskEntity)
        fetchTasks()
    }

    fun deleteAllTasks() = viewModelScope.launch {
        taskRepository.deleteAll()
        fetchTasks()
    }

    fun setState(state: TaskState) {
        _taskLiveData.value = state
    }

    fun updateTask(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.update(taskEntity)
        fetchTasks()
    }
}