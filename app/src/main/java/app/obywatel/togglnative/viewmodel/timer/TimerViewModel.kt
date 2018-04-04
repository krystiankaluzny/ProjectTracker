package app.obywatel.togglnative.viewmodel.timer

import app.obywatel.togglnative.model.entity.Workspace
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class TimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    private val workspaces: List<Workspace> = timerService.getStoredWorkspaces()

    init {
        launch(CommonPool) { timerService.fetchWorkspaces() }
    }

    fun getWorkspaceName(position: Int): String = workspaces[position].name

    fun getWorkspaceId(position: Int): Long = workspaces[position].id

    fun getWorkspacesCount(): Int = workspaces.size
}