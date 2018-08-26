package app.obywatel.togglnative.view.timer

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.databinding.TimerActivityBinding
import app.obywatel.togglnative.di.TimerViewModelModule
import app.obywatel.togglnative.view.BaseActivity
import app.obywatel.togglnative.viewmodel.timer.DailyTimerViewModel
import kotlinx.android.synthetic.main.timer_activity_content.*
import javax.inject.Inject

class TimerActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TimerActivity::class.java)
        }
    }

    @Inject lateinit var dailyTimerViewModel: DailyTimerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpProjectList()
        setUpListeners()
    }

    override fun inject() {
        TogglNativeApp.getUserComponent()
            .plus(TimerViewModelModule())
            .inject(this)
    }

    override fun setUpBinding() {

        val binding: TimerActivityBinding = DataBindingUtil.setContentView(this, R.layout.timer_activity)
        binding.viewModel = dailyTimerViewModel
    }

    private fun setUpProjectList() {
        val projectAdapter = ProjectAdapter(dailyTimerViewModel)

        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)

        recycleView.layoutManager = linearLayoutManager
        recycleView.addItemDecoration(dividerItemDecoration)
        recycleView.adapter = projectAdapter
    }

    private fun setUpListeners() {
    }
}
