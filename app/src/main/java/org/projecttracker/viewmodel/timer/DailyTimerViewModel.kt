package org.projecttracker.viewmodel.timer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.projecttracker.event.ProjectsUpdatedEvent
import org.projecttracker.model.entity.TimeEntry
import org.projecttracker.model.service.timer.TimerService
import org.projecttracker.viewmodel.BaseViewModel
import org.projecttracker.viewmodel.NetworkStateMonitor
import org.slf4j.LoggerFactory
import org.threeten.bp.Duration

class DailyTimerViewModel(private val timerService: TimerService, private val networkStateMonitor: NetworkStateMonitor) : BaseViewModel() {

    companion object {
        private val logger = LoggerFactory.getLogger(DailyTimerViewModel::class.java)
    }

    val allProjectsTimer = ObservableTimer()

    private var projectViewModels: List<SingleProjectViewModel> = emptyList()

    init {
        GlobalScope.launch(Dispatchers.Main) {
            updateViewModels()

            networkStateMonitor.ifConnected {
                withContext(Dispatchers.Default) {
                    timerService.fetchTodayTimeEntries()
                }
                updateViewModels()
            }
        }
    }

    fun projectsCount() = projectViewModels.size
    fun singleProjectViewModel(position: Int) = projectViewModels[position]

    fun toggleProject(projectViewModel: SingleProjectViewModel) {

        if(invalidNetworkState(networkStateMonitor)) return

        GlobalScope.launch(Dispatchers.Main) {

            val currentWasRunning = projectViewModel.projectRunning.get()

            allProjectsTimer.stop()
            projectViewModels.forEach { it.stopCounting() }

            if (!currentWasRunning) {
                allProjectsTimer.start()
                projectViewModel.startCounting()
            }

            withContext(Dispatchers.Default) {
                if (currentWasRunning) {
                    timerService.stopTimerForProject(projectViewModel.project)
                    timerService.fetchTodayTimeEntries()
                } else {
                    timerService.startTimerForProject(projectViewModel.project)
                }
            }
            if(currentWasRunning) {
                updateViewModels()
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

            it.projectTimer.duration = todayProjectDuration

            totalTodayDuration += todayProjectDuration
        }

        allProjectsTimer.duration = totalTodayDuration
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