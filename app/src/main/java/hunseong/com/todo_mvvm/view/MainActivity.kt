package hunseong.com.todo_mvvm.view

import android.animation.Animator
import android.app.Activity
import android.app.AlertDialog
import android.app.DirectAction
import android.content.DialogInterface
import android.graphics.*
import android.graphics.drawable.Drawable
import android.icu.text.RelativeDateTimeFormatter
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.START,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean = false

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.LEFT) adapter.removeTask(viewHolder.layoutPosition)
            else {
                showModifyDialog(adapter.tasks[viewHolder.layoutPosition])
            }
        }
    }


    override fun initViews() = with(binding) {
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(taskRecyclerView)
        adapter = TaskAdapter(viewModel).apply {
            onClickListener = {
                viewModel.updateTask(it.copy(isCompleted = !it.isCompleted))
            }
        }
        taskRecyclerView.adapter = adapter
        taskRecyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
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
            if (title.isBlank()) {
                Toast.makeText(this@MainActivity, "Write your task!", Toast.LENGTH_SHORT).show()
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

        completeTaskDeleteButton.setOnClickListener {
            viewModel.deleteCompleteTasks()
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
            is TaskState.EmptyCompletedTask -> handleEmptyCompletedTask()
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

            (taskRecyclerView.adapter as? TaskAdapter)?.tasks = state.tasks
            taskRecyclerView.adapter?.notifyDataSetChanged()
            val allTasksSize = state.tasks.size.toFloat()
            val completedTaskSize = state.tasks.filter { it.isCompleted }.size.toFloat()
            val completedPercent = completedTaskSize / allTasksSize * 100f
            val result = "Completed : %.1f".format(completedPercent) + "%"
            completeRatingTextView.text = result
        }

    }

    private fun handleEmptyCompletedTask() {
        Toast.makeText(this, "No tasks completed!", Toast.LENGTH_SHORT).show()
    }

    private fun handleErrorState(state: TaskState.Error) = with(binding) {
        progressBar.isGone = true
        emptyTaskTextView.isGone = true
        taskRecyclerView.isGone = true
        Toast.makeText(this@MainActivity, getString(state.message, state.error), Toast.LENGTH_SHORT)
            .show()
    }

    private fun showModifyDialog(taskEntity: TaskEntity) {
        val view = LayoutInflater.from(this).inflate(R.layout.modify_dialog_edit_text, null)
        val editText: EditText = view.findViewById(R.id.modifyEditText)
        editText.setText(taskEntity.title)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Modify Task")
            .setView(view)
            .setPositiveButton("Modify") { _, _ ->
                if (editText.text.isNullOrBlank()) {
                    Toast.makeText(this, "Please Edit Your Task.", Toast.LENGTH_SHORT).show()
                    viewModel.fetchTasks()
                } else {
                    viewModel.updateTask(taskEntity.copy(
                        title = editText.text.toString()
                    ))
                }
            }
            .setNegativeButton("Cancel") { _, _ -> viewModel.fetchTasks() }
            .create()

        dialog.setOnShowListener { _ ->
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.dark_red))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.dark_gray))
        }

        dialog.show()

    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
