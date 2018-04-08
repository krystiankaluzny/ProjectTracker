package app.obywatel.togglnative.viewmodel.user

import android.databinding.ObservableBoolean
import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.user.AddingUserService
import app.obywatel.togglnative.model.service.user.UserSelectionService
import app.obywatel.togglnative.model.util.ListenerGroup
import app.obywatel.togglnative.model.util.ListenerGroupConsumer
import app.obywatel.togglnative.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class UserViewModel(private val userSelectionService: UserSelectionService,
                    private val addingUserService: AddingUserService) : BaseViewModel() {

    companion object {
        private const val TAG = "UserViewModel"
    }

    private val addUserListenerConsumer = ListenerGroupConsumer<UpdateUserListener>()
    private val selectUserListenerConsumer = ListenerGroupConsumer<SelectUserListener>()
    private val userList: MutableList<User> = userSelectionService.getAllUsers()

    val searchingUserInProgress = ObservableBoolean(false)

    val updateUserListeners: ListenerGroup<UpdateUserListener> = addUserListenerConsumer
    val selectUserListeners: ListenerGroup<SelectUserListener> = selectUserListenerConsumer

    fun userCount() = userList.size
    fun singleUserViewModel(position: Int) = SingleUserViewModel(userList[position], this)

    fun addUserByApiToken(apiToken: String) = launch(UI) {

        try {
            searchingUserInProgress.set(true)

            val user: User? = async { addingUserService.addUserByApiToken(apiToken) }.await()

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
        async { userSelectionService.selectUser(user) }.await()
        selectUserListenerConsumer.accept { it.onUserSelected(user) }
    }

    private fun updateOrInsertUser(user: User) {

        val userPosition = userList.indexOfFirst { it.id == user.id }

        when {
            userPosition >= 0 -> {
                userList[userPosition] = user
                addUserListenerConsumer.accept { it.onUpdateUser(userPosition) }
            }
            else -> {
                userList.add(user)
                val position = userList.size - 1
                addUserListenerConsumer.accept { it.onAddUser(position) }
            }
        }
    }


}