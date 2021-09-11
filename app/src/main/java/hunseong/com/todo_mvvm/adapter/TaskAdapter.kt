package hunseong.com.todo_mvvm.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hunseong.com.todo_mvvm.data.entity.TaskEntity
import hunseong.com.todo_mvvm.databinding.ItemTaskBinding
import hunseong.com.todo_mvvm.viewmodel.MainViewModel

class TaskAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var tasks: List<TaskEntity> = emptyList()

    var onClickListener: ((TaskEntity) -> Unit)? = null

    fun removeTask(position: Int) {
        viewModel.deleteTask(tasks[position].id)
    }

    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity) = with(binding) {
            binding.root.setOnClickListener {
                onClickListener?.invoke(task)
            }
            taskTextView.text = task.title

            if(task.isCompleted) {
                checkImage.alpha = 1f
                binding.root.alpha = 0.5f
                binding.taskTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                checkImage.alpha = 0f
                binding.root.alpha = 1f
                binding.taskTextView.paintFlags = Paint.LINEAR_TEXT_FLAG
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size
}