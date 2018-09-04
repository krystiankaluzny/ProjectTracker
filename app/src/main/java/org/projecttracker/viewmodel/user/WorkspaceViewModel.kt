package org.projecttracker.viewmodel.user

import android.databinding.Observable
import android.databinding.ObservableInt
import org.slf4j.LoggerFactory
import org.projecttracker.model.entity.User
import org.projecttracker.model.entity.Workspace
import org.projecttracker.model.service.user.UserService
import org.projecttracker.model.util.ListenerGroup
import org.projecttracker.model.util.ListenerGroupConsumer
import org.projecttracker.viewmodel.ErrorViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class WorkspaceViewModel(private val userService: UserService, userViewModel: UserViewModel) : ErrorViewModel by userViewModel, SelectUserListener {

    companion object {
        private val logger = LoggerFactory.getLogger(WorkspaceViewModel::class.java)
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
        logger.debug("onSelectUser: $user")
        refreshWorkspaces(user)
    }

    private fun refreshWorkspaces(user: User) {

        getWorkspaces(user)
        updateWorkspacesListenerConsumer.accept { it.onUpdateWorkspaces() }

        launch(UI) {

            async {
                logger.trace("refreshWorkspaces: start fetching workspaces")
                userService.fetchWorkspaces(user)
                logger.trace("refreshWorkspaces: get workspaces after fetch")
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