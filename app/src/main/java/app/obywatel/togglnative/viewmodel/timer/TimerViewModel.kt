package app.obywatel.togglnative.viewmodel.timer

import app.obywatel.togglnative.model.entity.Project
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.viewmodel.BaseViewModel

class TimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val TAG = "TimerViewModel"
    }

    private var projects: List<Project> = emptyList()


    fun projectsCount() = projects.size
    fun singleProjectViewModel(position: Int) = SingleProjectViewModel(projects[position])

}