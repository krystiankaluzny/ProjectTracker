package org.projecttracker.viewmodel.user

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.projecttracker.model.entity.User
import org.projecttracker.model.service.user.UserService
import org.projecttracker.model.util.ListenerGroup
import org.projecttracker.model.util.ListenerGroupConsumer
import org.projecttracker.viewmodel.BaseViewModel
import org.slf4j.LoggerFactory

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
        updateUserListeners += workspaceViewModel
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

    fun addUserByApiToken(apiToken: String) = GlobalScope.launch(Dispatchers.Main) {

        try {
            searchingUserInProgress.set(true)

            val user: User? = withContext(Dispatchers.Default) { userService.addUserByApiToken(apiToken) }

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

    private fun selectUser(user: User) = GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.Default) { userService.selectUser(user) }
        selectUserListenerConsumer.accept { it.onSelectUser(user) }
    }

    private fun updateOrInsertUser(user: User) {

        val userPosition = userList.indexOfFirst { it.id == user.id }

        when {
            userPosition >= 0 -> {
                userList[userPosition] = user
                updateUserListenerConsumer.accept { it.onUpdateUser(userPosition, user) }
            }
            else -> {
                userList.add(user)
                val position = userList.size - 1
                updateUserListenerConsumer.accept { it.onAddUser(position, user) }
            }
        }
    }

    private inner class OnUserPositionChanged : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            userList.getOrNull(selectedUserPosition.get())?.let { selectUser(it) }
        }
    }
}