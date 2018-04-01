package app.obywatel.togglnative.view.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.di.TimerViewModelModule
import app.obywatel.togglnative.model.service.timer.TimerService
import javax.inject.Inject

class TimerActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TimerActivity::class.java)
        }
    }

    @Inject lateinit var timerService: TimerService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TogglNativeApp.getUserComponent(this)
            .plus(TimerViewModelModule())
            .inject(this)

        setContentView(R.layout.timer_activity)

        timerService.getWorkspaces()
    }
}
