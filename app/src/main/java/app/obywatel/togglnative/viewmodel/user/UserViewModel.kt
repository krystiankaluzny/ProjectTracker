package app.obywatel.togglnative.viewmodel.user

import android.databinding.ObservableBoolean
import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.user.UserService
import app.obywatel.togglnative.model.util.ListenerGroup
import app.obywatel.togglnative.model.util.ListenerGroupConsumer
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class UserViewModel(private val userService: UserService) : BaseViewModel() {

    companion object {
        private const val TAG = "UserViewModel"
    }

    private val updateUserListenerConsumer = ListenerGroupConsumer<UpdateUserListener>()
    private val selectUserListenerConsumer = ListenerGroupConsumer<SelectUserListener>()
    val updateUserListeners: ListenerGroup<UpdateUserListener> = updateUserListenerConsumer
    val selectUserListeners: ListenerGroup<SelectUserListener> = selectUserListenerConsumer

    private val userList: MutableList<User> = userService.getAllUsers()
    val searchingUserInProgress = ObservableBoolean(false)
    val workspaceViewModel = WorkspaceViewModel(userService)

    init {
        userList.getOrNull(0)?.let {
            workspaceViewModel.onUserSelected(it)
        }
    }

    fun userCount() = userList.size
    fun getUserName(position: Int) = userList.getOrNull(position)?.fullName ?: ""
    fun getUserId(position: Int) = userList.getOrNull(position)?.id ?: -1L

    fun addUserByApiToken(apiToken: String) = launch(UI) {

        try {
            searchingUserInProgress.set(true)

            val user: User? = async { userService.addUserByApiToken(apiToken) }.await()

            when (user) {
                null -> Log.w(TAG, "Null user")
                else -> updateOrInsertUser(user)
            }
        }
        catch (e: Exception) {
            Log.e(TAG, "addUserByApiToken", e)
            blinkException(e)
        }
        finally {
            searchingUserInProgress.set(false)
        }
    }

    fun selectUser(user: User) = launch(UI) {
        async { userService.selectUser(user) }.await()
        selectUserListenerConsumer.accept { it.onUserSelected(user) }
    }

    private fun updateOrInsertUser(user: User) {

        val userPosition = userList.indexOfFirst { it.id == user.id }

        when {
            userPosition >= 0 -> {
                userList[userPosition] = user
                updateUserListenerConsumer.accept { it.onUpdateUser(userPosition) }
            }
            else -> {
                userList.add(user)
                val position = userList.size - 1
                updateUserListenerConsumer.accept { it.onAddUser(position) }
            }
        }
    }


}