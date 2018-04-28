package app.obywatel.togglnative.viewmodel.user

import android.databinding.Observable
import android.databinding.ObservableInt
import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.Workspace
import app.obywatel.togglnative.model.service.user.UserService
import app.obywatel.togglnative.model.util.ListenerGroup
import app.obywatel.togglnative.model.util.ListenerGroupConsumer
import app.obywatel.togglnative.viewmodel.ErrorViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class WorkspaceViewModel(private val userService: UserService, userViewModel: UserViewModel) : ErrorViewModel by userViewModel, SelectUserListener {

    companion object {
        private const val TAG = "WorkspaceViewModel"
    }

    private val updateWorkspacesListenerConsumer = ListenerGroupConsumer<UpdateWorkspacesListener>()
    val updateWorkspacesListeners: ListenerGroup<UpdateWorkspacesListener> = updateWorkspacesListenerConsumer

    private var workspaces: List<Workspace> = emptyList()
    private lateinit var selectedUser: User

    val selectedWorkspacePosition = ObservableInt(0)

    init {
        selectedWorkspacePosition.addOnPropertyChangedCallback(OnWorkspacePositionChanged())
    }

    fun getWorkspacesCount(): Int = workspaces.size
    fun getWorkspaceName(position: Int): String = workspaces.getOrNull(position)?.name ?: ""
    fun getWorkspaceId(position: Int): Long = workspaces.getOrNull(position)?.id ?: -1

    override fun onSelectUser(user: User) {
        selectedUser = user
        Log.d(TAG, "onSelectUser: $user")
        refreshWorkspaces(user)
    }

    private fun refreshWorkspaces(user: User) {

        getWorkspaces(user)
        updateWorkspacesListenerConsumer.accept { it.onUpdateWorkspaces() }

        launch(UI) {

            async {
                Log.d(TAG, "refreshWorkspaces: start fetching workspaces")
                userService.fetchWorkspaces(user)
                Log.d(TAG, "refreshWorkspaces: get workspaces after fetch")
                getWorkspaces(user)
            }.await()

            updateWorkspacesListenerConsumer.accept { it.onUpdateWorkspaces() }
        }
    }

    private fun getWorkspaces(user: User) {

        if (selectedUser != user) return

        workspaces = userService.getStoredWorkspaces(user)

        val activeWorkspaceIndex = workspaces.indexOfFirst { it.id == user.activeWorkspaceId }
        if (activeWorkspaceIndex > -1) {
            selectedWorkspacePosition.set(activeWorkspaceIndex)
        }
    }

    private inner class OnWorkspacePositionChanged : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            workspaces.getOrNull(selectedWorkspacePosition.get())?.let {
                userService.setActiveWorkspace(selectedUser, it)
            }
        }
    }
}