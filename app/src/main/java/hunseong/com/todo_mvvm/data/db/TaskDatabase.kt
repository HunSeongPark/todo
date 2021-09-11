package hunseong.com.todo_mvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hunseong.com.todo_mvvm.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "TaskDatabase.db"
    }

    abstract fun taskDao(): TaskDao
}