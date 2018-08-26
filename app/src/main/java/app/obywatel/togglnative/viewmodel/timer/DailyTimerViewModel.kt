package app.obywatel.togglnative.viewmodel.timer

import android.databinding.ObservableField
import app.obywatel.togglnative.model.entity.Project
import app.obywatel.togglnative.model.entity.TimeEntry
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.model.util.ListenerGroup
import app.obywatel.togglnative.model.util.ListenerGroupConsumer
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.Duration

class DailyTimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val TAG = "DailyTimerViewModel"
    }

    private val updateProjectsListenerConsumer = ListenerGroupConsumer<UpdateProjectsListener>()
    val updateProjectsListeners: ListenerGroup<UpdateProjectsListener> = updateProjectsListenerConsumer

    var allProjectsDuration = ObservableField<String>("00:00:00")

    private var projects: List<Project> = timerService.getStoredProjects()
    private var projectViewModels: List<SingleProjectViewModel> = emptyList()

    init {
        launch {
            updateViewModels()
            timerService.fetchTodayTimeEntries()
            updateViewModels()
        }
    }

    fun projectsCount() = projects.size
    fun singleProjectViewModel(position: Int) = projectViewModels[position]

    private fun updateViewModels() {

        projectViewModels = timerService.getStoredProjects()
            .map { SingleProjectViewModel(it) }

        updateProjectsListenerConsumer.accept { it.onUpdateProjects() }

        var totalTodayDuration = Duration.ZERO

        projectViewModels.forEach {
            val projectTimeEntries = timerService.getStoredTimeEntriesForToday(it.project)
            val todayProjectDuration = calculateTotalDuration(projectTimeEntries)

            it.setDuration(todayProjectDuration)

            totalTodayDuration += todayProjectDuration
        }

        allProjectsDuration.set("%02d:%02d:%02d".format(
            totalTodayDuration.seconds / 3600,
            (totalTodayDuration.seconds % 3600) / 60,
            totalTodayDuration.seconds % 60))
    }

    private fun calculateTotalDuration(timeEntries: List<TimeEntry>): Duration {
        var total = Duration.ZERO

        for (timeEntry in timeEntries) {
            timeEntry.duration?.let {
                total += it
            }
        }

        return total
    }
}