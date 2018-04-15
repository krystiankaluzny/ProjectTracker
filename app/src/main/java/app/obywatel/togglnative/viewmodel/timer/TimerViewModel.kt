package app.obywatel.togglnative.viewmodel.timer

import android.databinding.Observable
import android.databinding.ObservableInt
import android.util.Log
import app.obywatel.togglnative.model.entity.Project
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

    private val updateWorkspacesListenerGroup = ListenerGroupConsumer<UpdateWorkspacesListener>()
    private var workspaces: List<Workspace> = timerService.getStoredWorkspaces()
    private var projects: List<Project> = if (workspaces.isNotEmpty()) timerService.getStoredProjects(workspaces[0]) else emptyList()

    val updateWorkspacesListener: ListenerGroup<UpdateWorkspacesListener> = updateWorkspacesListenerGroup
    val selectedWorkspacePosition = ObservableInt(0)

    init {
        selectedWorkspacePosition.addOnPropertyChangedCallback(OnWorkspacePositionChanged())

        refreshWorkspaces().invokeOnCompletion { refreshProjects() }
    }

    fun getWorkspaceName(position: Int): String = workspaces.getOrNull(position)?.name ?: ""

    fun getWorkspaceId(position: Int): Long = workspaces.getOrNull(position)?.id ?: -1

    fun getWorkspacesCount(): Int = workspaces.size

    fun projectsCount() = projects.size
    fun singleProjectViewModel(position: Int) = SingleProjectViewModel(projects[position])

    private fun refreshWorkspaces() = launch(UI) {

        workspaces = async {
            Log.d(TAG, "refreshWorkspaces: start fetching workspaces")
            timerService.fetchWorkspaces()

            Log.d(TAG, "refreshWorkspaces: get workspaces after fetch")
            timerService.getStoredWorkspaces()
        }.await()

        updateWorkspacesListenerGroup.accept { it.onWorkspaceUpdate() }
    }

    private fun refreshProjects() = launch(UI) {
        workspaces.getOrNull(selectedWorkspacePosition.get())?.let {

            projects = async {
                Log.d(TAG, "refreshProjects: start fetching projects")
                timerService.fetchProjects(it)

                Log.d(TAG, "refreshProjects: get projects after fetch")
                timerService.getStoredProjects(it)
            }.await()
        }
    }

    private inner class OnWorkspacePositionChanged : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            refreshProjects()
        }
    }
}