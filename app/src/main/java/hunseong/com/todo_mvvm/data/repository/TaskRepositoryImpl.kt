package hunseong.com.todo_mvvm.data.repository

import hunseong.com.todo_mvvm.data.db.TaskDao
import hunseong.com.todo_mvvm.data.entity.TaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val taskDao: TaskDao
) : TaskRepository {

    override suspend fun getAllTasks(): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.getAllTasks()
    }

    override suspend fun insert(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        taskDao.insert(taskEntity)
    }

    override suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    override suspend fun update(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        taskDao.update(taskEntity)
    }

}