package org.projecttracker.viewmodel.timer

import android.databinding.ObservableField
import org.threeten.bp.Duration
import java.util.*
import kotlin.concurrent.fixedRateTimer

class ObservableTimer {

    private var countingTimer: Timer? = null
    val text = ObservableField<String>("00:00:00")
    var duration: Duration = Duration.ZERO
        set(value) {
            field = value
            this.text.set("%02d:%02d:%02d".format(
                value.seconds / 3600,
                (value.seconds % 3600) / 60,
                value.seconds % 60))
        }

    fun start() {
        countingTimer?.apply { cancel() }
        countingTimer = fixedRateTimer(initialDelay = 1000L, period = 1000L, action = {
            duration = duration.plusSeconds(1)
        })
    }

    fun stop() {
        countingTimer?.apply { cancel() }
        countingTimer = null
    }
}