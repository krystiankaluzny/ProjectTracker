package org.projecttracker.viewmodel.timer

import android.databinding.ObservableField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.projecttracker.event.ProjectsUpdatedEvent
import org.projecttracker.model.entity.TimeEntry
import org.projecttracker.model.service.timer.TimerService
import org.projecttracker.model.util.ListenerGroup
import org.projecttracker.model.util.ListenerGroupConsumer
import org.projecttracker.viewmodel.BaseViewModel
import org.slf4j.LoggerFactory
import org.threeten.bp.Duration

class DailyTimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val logger = LoggerFactory.getLogger(DailyTimerViewModel::class.java)
    }

    var allProjectsDuration = ObservableField<String>("00:00:00")

    private var projectViewModels: List<SingleProjectViewModel> = emptyList()

    init {
        GlobalScope.launch(Dispatchers.Main) {
            updateViewModels()
            withContext(Dispatchers.Default) {
                timerService.fetchTodayTimeEntries()
            }
            updateViewModels()
        }
    }

    fun projectsCount() = projectViewModels.size
    fun singleProjectViewModel(position: Int) = projectViewModels[position]

    fun toggleProject(projectViewModel: SingleProjectViewModel) {

        GlobalScope.launch(Dispatchers.Main) {

            val running = projectViewModel.projectRunning.get()

            projectViewModels.forEach { it.stopCounting() }

            if (!running) {
                projectViewModel.startCounting()
            }

            withContext(Dispatchers.Default) {
                if (running) {
//                    timerService.stopTimerForProject(projectViewModel.project)
                } else {
                    timerService.startTimerForProject(projectViewModel.project)
                }
            }
        }
    }

    private fun updateViewModels() {

        logger.trace("updateViewModels")

        val storedProjects = timerService.getStoredProjects()

        if (projectViewModels.size != storedProjects.size) {

            projectViewModels = storedProjects
                .map { SingleProjectViewModel(it) }

            EventBus.getDefault().post(ProjectsUpdatedEvent(storedProjects))

        } else {
            storedProjects.forEachIndexed { i, it ->
                projectViewModels[i].project = it
            }
        }

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