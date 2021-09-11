package hunseong.com.todo_mvvm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val isCompleted: Boolean = false
)
