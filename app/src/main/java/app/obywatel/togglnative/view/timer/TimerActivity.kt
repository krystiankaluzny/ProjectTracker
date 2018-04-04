package app.obywatel.togglnative.view.timer

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.databinding.TimerActivityBinding
import app.obywatel.togglnative.di.TimerViewModelModule
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.model.service.user.UserSelectionService
import app.obywatel.togglnative.view.BaseActivity
import app.obywatel.togglnative.viewmodel.timer.TimerViewModel
import app.obywatel.togglnative.viewmodel.timer.WorkspaceAdapter
import kotlinx.android.synthetic.main.timer_activity_content.*
import javax.inject.Inject

class TimerActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TimerActivity::class.java)
        }
    }

    @Inject lateinit var timerViewModel: TimerViewModel
    @Inject lateinit var timerService: TimerService
    @Inject lateinit var userSelectionService: UserSelectionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        button.setOnClickListener { userSelectionService.unselectUsers() }
        timerService.getStoredWorkspaces()
        workspaceSpinner.adapter = WorkspaceAdapter(this, timerViewModel)
    }

    override fun inject() {
        TogglNativeApp.getUserComponent(this)
            .plus(TimerViewModelModule())
            .inject(this)
    }

    override fun setUpBinding() {

        val binding: TimerActivityBinding = DataBindingUtil.setContentView(this, R.layout.timer_activity)
        binding.viewModel = timerViewModel
    }
}
