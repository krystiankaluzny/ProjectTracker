package org.projecttracker.viewmodel.user

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.projecttracker.event.UserAddedEvent
import org.projecttracker.event.UserSelectedEvent
import org.projecttracker.model.entity.User
import org.projecttracker.model.service.user.UserService
import org.projecttracker.viewmodel.BaseViewModel
import org.projecttracker.viewmodel.NetworkStateMonitor
import org.slf4j.LoggerFactory

class UserViewModel(private val userService: UserService, private val networkStateMonitor: NetworkStateMonitor) : BaseViewModel() {

    companion object {
        private val logger = LoggerFactory.getLogger(UserViewModel::class.java)
    }

    private val userList: MutableList<User> = userService.getAllUsers()
    val searchingUserInProgress = ObservableBoolean(false)
    val workspaceViewModel = WorkspaceViewModel(userService, this, networkStateMonitor)
    val selectedUserPosition = ObservableInt(0)

    init {
        selectedUserPosition.addOnPropertyChangedCallback(OnUserPositionChanged())
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

            if (indexOfUser == selectedUserPosition.get()) {
                EventBus.getDefault().post(UserSelectedEvent(user))
            } else {
                selectedUserPosition.set(indexOfUser)
            }
        }
    }

    fun addUserByApiToken(apiToken: String) = GlobalScope.launch(Dispatchers.Main) {

        if (invalidNetworkState(networkStateMonitor)) return@launch

        try {
            searchingUserInProgress.set(true)

            val user: User? = withContext(Dispatchers.Default) { userService.addUserByApiToken(apiToken) }

            when (user) {
                null -> logger.warn("Null user")
                else -> updateOrInsertUser(user)
            }
        } catch (e: Exception) {
            logger.error("addUserByApiToken", e)
            blinkException(e)
        } finally {
            searchingUserInProgress.set(false)
        }
    }

    private fun selectUser(user: User) = GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.Default) { userService.selectUser(user) }
        EventBus.getDefault().post(UserSelectedEvent(user))
    }

    private fun updateOrInsertUser(user: User) {

        val userPosition = userList.indexOfFirst { it.id == user.id }

        when {
            userPosition >= 0 -> {
                userList[userPosition] = user
                EventBus.getDefault().post(UserAddedEvent(userPosition, user))
            }
            else -> {
                userList.add(user)
                val position = userList.size - 1
                EventBus.getDefault().post(UserAddedEvent(position, user))
            }
        }
    }

    private inner class OnUserPositionChanged : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            userList.getOrNull(selectedUserPosition.get())?.let { selectUser(it) }
        }
    }
}