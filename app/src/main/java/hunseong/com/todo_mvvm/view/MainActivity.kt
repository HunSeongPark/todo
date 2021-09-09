package hunseong.com.todo_mvvm.view

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import hunseong.com.todo_mvvm.databinding.ActivityMainBinding
import hunseong.com.todo_mvvm.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {


    override val viewModel: MainViewModel
        get() = TODO("Not yet implemented")


    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


    override fun initViews() {

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
                    }
                    override fun onAnimationCancel(animation: Animator?) = Unit
                    override fun onAnimationRepeat(animation: Animator?) = Unit
                }).duration = 300
        }
    }

    override fun observeData() {
        TODO("Not yet implemented")
    }
}