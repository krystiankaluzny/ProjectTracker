package org.projecttracker.viewmodel.timer

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import org.projecttracker.model.entity.Project
import org.threeten.bp.Duration
import kotlin.concurrent.fixedRateTimer

class SingleProjectViewModel(project: Project) {

    val projectName = ObservableField<String>(project.name)
    val projectColor = ObservableInt(project.color)
    val projectDuration = ObservableField<String>("00:00:00")
    val projectRunning = ObservableBoolean(false)
    val projectTimer = ObservableTimer()

    internal var project: Project = project
        set(value) {
            field = value
            this.projectName.set(value.name)
            this.projectColor.set(value.color)
        }

    internal fun startCounting() {
        projectTimer.start()
        projectRunning.set(true)
    }

    fun stopCounting() {
        projectTimer.stop()
        projectRunning.set(false)
    }
}
