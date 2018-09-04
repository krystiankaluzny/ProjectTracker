package org.projecttracker.viewmodel.timer

import android.databinding.ObservableField
import org.projecttracker.model.entity.TimeEntry
import org.projecttracker.model.service.timer.TimerService
import org.projecttracker.model.util.ListenerGroup
import org.projecttracker.model.util.ListenerGroupConsumer
import org.projecttracker.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.Duration

class DailyTimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val TAG = "DailyTimerViewModel"
    }

    private val updateProjectsListenerConsumer = ListenerGroupConsumer<UpdateProjectsListener>()
    val updateProjectsListeners: ListenerGroup<UpdateProjectsListener> = updateProjectsListenerConsumer

    var allProjectsDuration = ObservableField<String>("00:00:00")

    private var projectViewModels: List<SingleProjectViewModel> = emptyList()

    init {
        launch(UI) {
            updateViewModels()
            async {
                timerService.fetchTodayTimeEntries()
            }.await()
            updateViewModels()
        }
    }

    fun projectsCount() = projectViewModels.size
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