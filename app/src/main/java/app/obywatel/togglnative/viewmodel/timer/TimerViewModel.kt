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
    private var projects: List<Project> = emptyList()

    val updateWorkspacesListener: ListenerGroup<UpdateWorkspacesListener> = updateWorkspacesListenerGroup
    val selectedWorkspacePosition = ObservableInt(0)

    init {
        selectedWorkspacePosition.addOnPropertyChangedCallback(OnWorkspacePositionChanged())

    }

    fun projectsCount() = projects.size
    fun singleProjectViewModel(position: Int) = SingleProjectViewModel(projects[position])


//    private fun refreshProjects() = launch(UI) {
//        workspaces.getOrNull(selectedWorkspacePosition.get())?.let {
//
//            projects = async {
//                Log.d(TAG, "refreshProjects: start fetching projects")
//                timerService.fetchProjects(it)
//
//                Log.d(TAG, "refreshProjects: get projects after fetch")
//                timerService.getStoredProjects(it)
//            }.await()
//        }
//    }

    private inner class OnWorkspacePositionChanged : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//            refreshProjects()
        }
    }
}