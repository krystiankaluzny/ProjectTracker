package app.obywatel.togglnative.viewmodel.user

import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.Workspace
import app.obywatel.togglnative.model.service.user.UserService
import app.obywatel.togglnative.viewmodel.ErrorViewModel
import kotlinx.coroutines.experimental.launch

class WorkspaceViewModel(private val userService: UserService, userViewModel: UserViewModel) : ErrorViewModel by userViewModel, SelectUserListener {

    companion object {
        private const val TAG = "WorkspaceViewModel"
    }

    private var workspaces: List<Workspace> = emptyList()

    fun getWorkspacesCount(): Int = workspaces.size
    fun getWorkspaceName(position: Int): String = workspaces.getOrNull(position)?.name ?: ""
    fun getWorkspaceId(position: Int): Long = workspaces.getOrNull(position)?.id ?: -1

    override fun onUserSelected(user: User) {
        Log.d(TAG, "onUserSelected: $user")
        refreshWorkspaces(user)
    }

    private fun refreshWorkspaces(user: User) {

        workspaces = userService.getStoredWorkspaces(user)

        launch {

            Log.d(TAG, "refreshWorkspaces: start fetching workspaces")
            userService.fetchWorkspaces(user)
            Log.d(TAG, "refreshWorkspaces: get workspaces after fetch")

            workspaces = userService.getStoredWorkspaces(user)
        }
    }
}