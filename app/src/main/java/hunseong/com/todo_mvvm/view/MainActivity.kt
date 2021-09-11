package hunseong.com.todo_mvvm.view

import android.animation.Animator
import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import hunseong.com.todo_mvvm.R
import hunseong.com.todo_mvvm.adapter.TaskAdapter
import hunseong.com.todo_mvvm.data.entity.TaskEntity
import hunseong.com.todo_mvvm.databinding.ActivityMainBinding
import hunseong.com.todo_mvvm.viewmodel.MainViewModel
import hunseong.com.todo_mvvm.viewmodel.state.TaskState
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.round
import kotlin.math.roundToInt

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {


    override val viewModel by viewModel<MainViewModel>()


    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var adapter: TaskAdapter


    override fun initViews() = with(binding) {

    }

    override fun bindView() = with(binding) {
        cancelButton.setOnClickListener {
            informationLayout.animate().alpha(0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) = Unit
                    override fun onAnimationEnd(animation: Animator?) {
                        informationLayout.isGone = true
                        popupBackgroundView.isGone = true
                        taskEditText.isEnabled = true
                        fetchJob = viewModel.fetchTasks()
                    }

                    override fun onAnimationCancel(animation: Animator?) = Unit
                    override fun onAnimationRepeat(animation: Animator?) = Unit
                }).duration = 300
        }


        addButton.setOnClickListener {
            val title = taskEditText.text.toString()
            if (title.isEmpty()) {
                Toast.makeText(this@MainActivity, "할 일을 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.insertTask(TaskEntity(
                id = System.currentTimeMillis(),
                title = title,
            ))
            taskEditText.text.clear()
            taskEditText.clearFocus()
            mainLayout.requestFocus()

            hideKeyboard()
        }

        deleteAllButton.setOnClickListener {
            viewModel.deleteAllTasks()
        }
    }

    override fun observeData() = viewModel.taskLiveData.observe(this) {
        when (it) {
            is TaskState.Uninitialized -> Unit
            is TaskState.Loading -> handleLoadingState()
            is TaskState.Success -> handleSuccessState(it)
            is TaskState.Error -> handleErrorState(it)
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
        taskRecyclerView.isGone = true
    }

    private fun handleSuccessState(state: TaskState.Success) = with(binding) {
        progressBar.isGone = true

        if (state.tasks.isNullOrEmpty()) {
            emptyTaskTextView.isVisible = true
            taskRecyclerView.isGone = true
        } else {
            emptyTaskTextView.isGone = true
            taskRecyclerView.isVisible = true
            if (::adapter.isInitialized.not()) {
                initRecyclerView(state.tasks)
            }
            (taskRecyclerView.adapter as? TaskAdapter)?.tasks = state.tasks
            taskRecyclerView.adapter?.notifyDataSetChanged()
            val allTasksSize = state.tasks.size.toFloat()
            val completedTaskSize = state.tasks.filter { it.isCompleted }.size.toFloat()
            val completedPercent = completedTaskSize / allTasksSize * 100f
            val result = "달성률 : %.1f".format(completedPercent) + "%"
            completeRatingTextView.text = result
        }

    }

    private fun initRecyclerView(tasks: List<TaskEntity>) = with(binding.taskRecyclerView) {
        adapter = TaskAdapter().apply {
            onClickListener = {
                viewModel.updateTask(it.copy(isCompleted = !it.isCompleted))
            }
        }

        adapter = adapter
        layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
    }

    private fun handleErrorState(state: TaskState.Error) = with(binding) {
        progressBar.isGone = true
        emptyTaskTextView.isGone = true
        taskRecyclerView.isGone = true
        Toast.makeText(this@MainActivity, getString(state.message, state.error), Toast.LENGTH_SHORT)
            .show()
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
