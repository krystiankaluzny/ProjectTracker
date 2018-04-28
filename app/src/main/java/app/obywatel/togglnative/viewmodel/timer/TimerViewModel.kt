package app.obywatel.togglnative.viewmodel.timer

import app.obywatel.togglnative.model.entity.Project
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.launch

class TimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val TAG = "TimerViewModel"
    }

    private var projects: List<Project> = timerService.getStoredProjects()

    init {
        launch {
            timerService.fetchTimeEntries()
        }
    }

    fun projectsCount() = projects.size
    fun singleProjectViewModel(position: Int) = SingleProjectViewModel(projects[position])

}