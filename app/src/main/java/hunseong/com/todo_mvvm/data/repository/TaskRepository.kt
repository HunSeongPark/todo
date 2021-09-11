package hunseong.com.todo_mvvm.data.repository

import hunseong.com.todo_mvvm.data.entity.TaskEntity

interface TaskRepository {

    suspend fun getAllTasks(): List<TaskEntity>

    suspend fun insert(taskEntity: TaskEntity)

    suspend fun deleteAll()

    suspend fun update(taskEntity: TaskEntity)
}