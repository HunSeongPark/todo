package hunseong.com.todo_mvvm.di

import hunseong.com.todo_mvvm.data.repository.TaskRepository
import hunseong.com.todo_mvvm.data.repository.TaskRepositoryImpl
import hunseong.com.todo_mvvm.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

//    Dispatcher
    single { Dispatchers.IO }

//    ViewModel
    viewModel { MainViewModel(get()) }

//    Repository
    single<TaskRepository> { TaskRepositoryImpl(get(),get()) }

//    DB
    single { provideDB(androidApplication()) }
    single { provideTaskDao(get()) }


}