package app.obywatel.togglnative.viewmodel.timer

import app.obywatel.togglnative.model.entity.Workspace
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.model.util.ListenerGroup
import app.obywatel.togglnative.model.util.ListenerGroupConsumer
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class TimerViewModel(private val timerService: TimerService) : BaseViewModel() {

    companion object {
        private val TAG = "TimerViewModel"
    }

    init {
        launch(UI) {
            workspaces = async { updateAndGetWorkspaces() }.await()

            updateWorkspacesListenerGroup.accept { it.onWorkspaceUpdate() }
        }
    }

    private val updateWorkspacesListenerGroup = ListenerGroupConsumer<UpdateWorkspacesListener>()
    private var workspaces: List<Workspace> = timerService.getStoredWorkspaces()

    val updateWorkspacesListener: ListenerGroup<UpdateWorkspacesListener> = updateWorkspacesListenerGroup

    fun getWorkspaceName(position: Int): String = workspaces[position].name

    fun getWorkspaceId(position: Int): Long = workspaces[position].id

    fun getWorkspacesCount(): Int = workspaces.size

    private fun updateAndGetWorkspaces(): MutableList<Workspace> {
        timerService.fetchWorkspaces()
        return timerService.getStoredWorkspaces()
    }
}