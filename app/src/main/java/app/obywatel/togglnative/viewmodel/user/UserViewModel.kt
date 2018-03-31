package app.obywatel.togglnative.viewmodel.user

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.UserService
import app.obywatel.togglnative.model.util.ListenerGroup
import app.obywatel.togglnative.model.util.ListenerGroupConsumer
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class UserViewModel(private val userService: UserService) {

    companion object {
        private const val TAG = "UserViewModel"
        private const val ERROR_MESSAGE_SHOW_TIME = 10_000L
    }

    private val addUserListenerConsumer = ListenerGroupConsumer<AddUserListener>()
    private val selectUserListenerConsumer = ListenerGroupConsumer<SelectUserListener>()
    private val userList: MutableList<User> = userService.getAllUsers()

    val searchingUserInProgress = ObservableBoolean(false)
    val errorMessageVisible = ObservableBoolean(false)
    val errorMessage = ObservableField<String>("")
    val addUserListeners: ListenerGroup<AddUserListener> = addUserListenerConsumer
    val selectUserListeners: ListenerGroup<SelectUserListener> = selectUserListenerConsumer

    fun userCount() = userList.size
    fun singleUserViewModel(position: Int) = SingleUserViewModel(userList[position], this)
    fun onClickErrorMessage() = hideErrorMessage()

    fun addUserByApiToken(apiToken: String) = launch(UI) {

        try {
            searchingUserInProgress.set(true)

            val user: User? = async(CommonPool) { userService.addUserByApiToken(apiToken) }.await()

            if (user == null) {
                Log.w("UserViewModel", "Null user")
            } else {
                userList.add(user)
                val position = userList.size - 1
                addUserListenerConsumer.accept { it.onAddUser(position) }
            }
        }
        catch (e: Exception) {
            launch(UI) {
                showErrorMessage(e.message)
                delay(ERROR_MESSAGE_SHOW_TIME)
                hideErrorMessage()
            }
        }
        finally {
            searchingUserInProgress.set(false)
        }
    }

    internal fun selectUser(user: User) = launch(UI) {
                async(CommonPool) { userService.selectUser(user) }.await()
                selectUserListenerConsumer.accept { it.onUserSelected() }
            }


    private fun showErrorMessage(msg: String?) {
        errorMessage.set(msg)
        errorMessageVisible.set(true)
    }

    private fun hideErrorMessage() {
        errorMessageVisible.set(false)
        errorMessage.set("")
    }

}