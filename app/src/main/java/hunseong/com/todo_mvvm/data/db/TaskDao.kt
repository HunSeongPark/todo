package hunseong.com.todo_mvvm.data.db

import androidx.room.*
import hunseong.com.todo_mvvm.data.entity.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity")
    suspend fun getAllTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM TaskEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query ("DELETE FROM TaskEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(taskEntity: TaskEntity)
}