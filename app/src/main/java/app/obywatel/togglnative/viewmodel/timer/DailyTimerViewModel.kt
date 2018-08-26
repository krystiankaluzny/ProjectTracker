package app.obywatel.togglnative.viewmodel.timer

import android.databinding.ObservableField
import app.obywatel.togglnative.model.entity.Project
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.Duration

class DailyTimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val TAG = "DailyTimerViewModel"
    }

    var totalDurationStr = ObservableField<String>("00:00:00")

    private var projects: List<Project> = timerService.getStoredProjects()

    init {
        launch {
            calculateTotalDuration()
            timerService.fetchTodayTimeEntries()
            calculateTotalDuration()
        }
    }

    fun projectsCount() = projects.size
    fun singleProjectViewModel(position: Int) = SingleProjectViewModel(projects[position])

    private fun calculateTotalDuration() {

        var total = Duration.ZERO

        for (timeEntry in timerService.getStoredTimeEntriesForToday()) {
            timeEntry.duration?.let {
                total += it
            }
        }

        totalDurationStr.set("%02d:%02d:%02d".format(
            total.seconds / 3600,
            (total.seconds % 3600) / 60,
            total.seconds % 60))
    }
}