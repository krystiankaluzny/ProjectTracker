package org.projecttracker.viewmodel.user

import android.databinding.Observable
import android.databinding.ObservableInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.projecttracker.event.UserAddedEvent
import org.projecttracker.event.UserSelectedEvent
import org.projecttracker.event.WorkspacesUpdatedEvent
import org.projecttracker.model.entity.User
import org.projecttracker.model.entity.Workspace
import org.projecttracker.model.service.user.UserService
import org.projecttracker.viewmodel.ErrorViewModel
import org.slf4j.LoggerFactory


class WorkspaceViewModel(private val userService: UserService, userViewModel: UserViewModel) : ErrorViewModel by userViewModel {

    companion object {
        private val logger = LoggerFactory.getLogger(WorkspaceViewModel::class.java)
    }

    private var workspaces: List<Workspace> = emptyList()
    private lateinit var selectedUser: User

    val selectedWorkspacePosition = ObservableInt(0)

    init {
        selectedWorkspacePosition.addOnPropertyChangedCallback(OnWorkspacePositionChanged())
    }

    fun getWorkspacesCount(): Int = workspaces.size
    fun getWorkspaceName(position: Int): String = workspaces.getOrNull(position)?.name ?: ""
    fun getWorkspaceId(position: Int): Long = workspaces.getOrNull(position)?.id ?: -1

    @Subscribe
    fun onUserSelected(userSelectedEvent: UserSelectedEvent) {
        selectedUser = userSelectedEvent.user
        logger.debug("onUserSelected: ${userSelectedEvent.user}")
        refreshWorkspaces(userSelectedEvent.user)
    }

    @Subscribe
    fun onUserAdded(userAddedEvent: UserAddedEvent) {
        logger.debug("onUserAdded: ${userAddedEvent.user}")
        if (userAddedEvent.position == 0) {
            selectedUser = userAddedEvent.user
            getWorkspaces(selectedUser)
            EventBus.getDefault().post(WorkspacesUpdatedEvent(workspaces))
        }
    }

    private fun refreshWorkspaces(user: User) {

        getWorkspaces(user)
        EventBus.getDefault().post(WorkspacesUpdatedEvent(workspaces))

        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.Default) {
                logger.trace("refreshWorkspaces: start fetching workspaces")
                userService.fetchWorkspaces(user)
                logger.trace("refreshWorkspaces: get workspaces after fetch")
                getWorkspaces(user)
            }

            EventBus.getDefault().post(WorkspacesUpdatedEvent(workspaces))
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