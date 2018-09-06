package org.projecttracker.viewmodel.timer

import android.databinding.ObservableField
import android.databinding.ObservableInt
import org.projecttracker.model.entity.Project
import org.threeten.bp.Duration

class SingleProjectViewModel(internal var project: Project) {

    val projectName = ObservableField<String>(project.name)
    val projectColor = ObservableInt(project.color)
    val projectDuration = ObservableField<String>("00:00:00")

    fun setProject(project: Project) {
        this.project = project
        this.projectName.set(project.name)
        this.projectColor.set(project.color)
    }

    fun setDuration(duration: Duration) {

        this.projectDuration.set("%02d:%02d:%02d".format(
            duration.seconds / 3600,
            (duration.seconds % 3600) / 60,
            duration.seconds % 60))
    }
}
