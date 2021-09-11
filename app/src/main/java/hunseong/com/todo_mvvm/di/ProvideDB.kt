package hunseong.com.todo_mvvm.di

import android.content.Context
import androidx.room.Room
import hunseong.com.todo_mvvm.data.db.TaskDatabase

fun provideDB(context: Context) : TaskDatabase =
    Room.databaseBuilder(context, TaskDatabase::class.java, TaskDatabase.DB_NAME).build()

fun provideTaskDao(database: TaskDatabase) = database.taskDao()

