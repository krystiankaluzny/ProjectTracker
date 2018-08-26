package app.obywatel.togglnative.viewmodel.timer

import android.databinding.ObservableField
import app.obywatel.togglnative.model.entity.Project
import org.threeten.bp.Duration

class SingleProjectViewModel(internal val project: Project) {

    val projectName: String = project.name
    val projectColor: Int = project.color
    var projectDuration = ObservableField<String>("00:00:00")

    fun setDuration(duration: Duration) {

        projectDuration.set("%02d:%02d:%02d".format(
            duration.seconds / 3600,
            (duration.seconds % 3600) / 60,
            duration.seconds % 60))
    }
}
