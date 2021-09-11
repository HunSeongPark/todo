package hunseong.com.todo_mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import hunseong.com.todo_mvvm.viewmodel.BaseViewModel
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding> : AppCompatActivity() {

    abstract val viewModel: VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    protected lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewBinding()
        setContentView(binding.root)
        initViews()
        bindView()
        observeData()
    }

    abstract fun initViews()

    abstract fun bindView()

    abstract fun observeData()

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }


}