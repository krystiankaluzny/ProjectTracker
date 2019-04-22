package org.projecttracker.view.timer

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import org.projecttracker.R
import org.projecttracker.ProjectTrackerApp
import org.projecttracker.databinding.TimerActivityBinding
import org.projecttracker.di.TimerViewModelModule
import org.projecttracker.view.BaseActivity
import org.projecttracker.viewmodel.timer.DailyTimerViewModel
import kotlinx.android.synthetic.main.timer_activity_content.*
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory
import javax.inject.Inject

class TimerActivity : BaseActivity() {

    companion object {
        private val logger = LoggerFactory.getLogger(TimerActivity::class.java)
        fun newIntent(context: Context): Intent {
            return Intent(context, TimerActivity::class.java)
        }
    }

    @Inject
    lateinit var dailyTimerViewModel: DailyTimerViewModel
    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.trace("on create start")
        super.onCreate(savedInstanceState)

        setUpProjectList()
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(projectAdapter)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(projectAdapter)
    }

    override fun inject() {
        ProjectTrackerApp.getUserComponent()
            .plus(TimerViewModelModule())
            .inject(this)
    }

    override fun setUpBinding() {

        val binding: TimerActivityBinding = DataBindingUtil.setContentView(this, R.layout.timer_activity)
        binding.viewModel = dailyTimerViewModel
    }

    private fun setUpProjectList() {
        projectAdapter = ProjectAdapter(dailyTimerViewModel)

        val layoutManager = GridLayoutManager(this, 2)

        recycleView.layoutManager = layoutManager
        recycleView.adapter = projectAdapter
    }

}
