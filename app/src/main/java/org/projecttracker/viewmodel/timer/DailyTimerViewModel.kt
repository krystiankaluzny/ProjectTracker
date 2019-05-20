package org.projecttracker.viewmodel.timer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.projecttracker.event.ProjectsUpdatedEvent
import org.projecttracker.event.TodayTimerDataRefreshedEvent
import org.projecttracker.model.entity.TimeEntry
import org.projecttracker.model.service.timer.TimerService
import org.projecttracker.viewmodel.BaseViewModel
import org.projecttracker.viewmodel.NetworkStateMonitor
import org.slf4j.LoggerFactory
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

class DailyTimerViewModel(private val timerService: TimerService, private val networkStateMonitor: NetworkStateMonitor) : BaseViewModel() {

    companion object {
        private val logger = LoggerFactory.getLogger(DailyTimerViewModel::class.java)
    }

    val allProjectsTimer = ObservableTimer()

    private var projectViewModels: List<SingleProjectViewModel> = emptyList()

    init {
        refresh()
    }

    fun projectsCount() = projectViewModels.size
    fun singleProjectViewModel(position: Int) = projectViewModels[position]

    fun refresh() {
        GlobalScope.launch(Dispatchers.Main) {
            updateViewModels()

            networkStateMonitor.ifConnected {
                withContext(Dispatchers.Default) {
                    timerService.fetchTodayTimeEntries()
                }
                updateViewModels()
            }

            EventBus.getDefault().post(TodayTimerDataRefreshedEvent())
        }
    }

    fun toggleProject(projectViewModel: SingleProjectViewModel) {

        if (invalidNetworkState(networkStateMonitor)) return

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
                    timerService.stopTimer()
                } else {
                    timerService.startTimerForProject(projectViewModel.project)
                }
            }

            updateViewModels()
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

        allProjectsTimer.stop()

        projectViewModels.forEach {

            val projectTimeEntries = timerService.getStoredTimeEntriesForToday(it.project)
            val finishedEntriesDuration = calculateFinishedEntriesDuration(projectTimeEntries)

            it.projectTimer.duration = finishedEntriesDuration
            totalTodayDuration += finishedEntriesDuration

            val runningTimeEntry = selectRunningTimeEntry(projectTimeEntries)

            if (runningTimeEntry != null) {
                val runningEntryDuration = Duration.between(runningTimeEntry.startDateTime, OffsetDateTime.now())

                it.projectTimer.duration += runningEntryDuration
                totalTodayDuration += runningEntryDuration

                it.startCounting()
                allProjectsTimer.start()

            } else {
                it.stopCounting()
            }
        }

        allProjectsTimer.duration = totalTodayDuration
    }


    private fun calculateFinishedEntriesDuration(timeEntries: List<TimeEntry>): Duration {
        var total = Duration.ZERO

        for (timeEntry in timeEntries) {
            timeEntry.duration?.let {
                if (it > Duration.ZERO) total += it
            }
        }

        return total
    }

    private fun selectRunningTimeEntry(projectTimeEntries: MutableList<TimeEntry>): TimeEntry? {

        return projectTimeEntries.find { it.endDateTime == null }
    }
}