package hunseong.com.todo_mvvm.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    abstract fun fetchTasks(): Job

}