package app.obywatel.togglnative.viewmodel.timer

import app.obywatel.togglnative.model.entity.Project

class SingleProjectViewModel(private val project: Project) {

    val projectName: String = project.name
    val projectColor: Int = project.color
}
