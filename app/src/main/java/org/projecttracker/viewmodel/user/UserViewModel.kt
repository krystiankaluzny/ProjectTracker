package org.projecttracker.viewmodel.user

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import org.slf4j.LoggerFactory
import org.projecttracker.model.entity.User
import org.projecttracker.model.service.user.UserService
import org.projecttracker.model.util.ListenerGroup
import org.projecttracker.model.util.ListenerGroupConsumer
import org.projecttracker.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class UserViewModel(private val userService: UserService) : BaseViewModel() {

    companion object {
        private val logger = LoggerFactory.getLogger(UserViewModel::class.java)
    }

    private val updateUserListenerConsumer = ListenerGroupConsumer<UpdateUserListener>()
    private val selectUserListenerConsumer = ListenerGroupConsumer<SelectUserListener>()
    val updateUserListeners: ListenerGroup<UpdateUserListener> = updateUserListenerConsumer
    val selectUserListeners: ListenerGroup<SelectUserListener> = selectUserListenerConsumer

    private val userList: MutableList<User> = userService.getAllUsers()
    val searchingUserInProgress = ObservableBoolean(false)
    val workspaceViewModel = WorkspaceViewModel(userService, this)
    val selectedUserPosition = ObservableInt(0)

    init {
        selectedUserPosition.addOnPropertyChangedCallback(OnUserPositionChanged())
        selectUserListeners += workspaceViewModel
    }

    fun userCount() = userList.size
    fun getUserName(position: Int) = userList.getOrNull(position)?.fullName ?: ""
    fun getUserId(position: Int) = userList.getOrNull(position)?.id ?: -1L

    fun showSelectedUser() {

        var selectedUser = userList.find { it.selected }

        if (selectedUser == null) {
            selectedUser = userList.getOrNull(0)
            selectedUser?.let { userService.selectUser(it) }
        }

        selectedUser?.let { user ->
            val indexOfUser = userList.indexOf(user)

            when (indexOfUser) {
                selectedUserPosition.get() -> selectUserListenerConsumer.accept { it.onSelectUser(user) }
                else -> selectedUserPosition.set(indexOfUser)
            }
        }
    }

    fun addUserByApiToken(apiToken: String) = launch(UI) {

        try {
            searchingUserInProgress.set(true)

            val user: User? = async { userService.addUserByApiToken(apiToken) }.await()

            when (user) {
                null -> logger.warn("Null user")
                else -> updateOrInsertUser(user)
            }
        }
        catch (e: Exception) {
            logger.error("addUserByApiToken", e)
            blinkException(e)
        }
        finally {
            searchingUserInProgress.set(false)
        }
    }

    private fun selectUser(user: User) = launch(UI) {
        async { userService.selectUser(user) }.await()
        selectUserListenerConsumer.accept { it.onSelectUser(user) }
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

    private inner class OnUserPositionChanged : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            userList.getOrNull(selectedUserPosition.get())?.let { selectUser(it) }
        }
    }
}