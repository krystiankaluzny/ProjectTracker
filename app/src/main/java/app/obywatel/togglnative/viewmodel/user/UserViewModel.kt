package app.obywatel.togglnative.viewmodel.user

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.UserService
import com.raizlabs.android.dbflow.kotlinextensions.select
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*

class UserViewModel(private val userService: UserService) {
    companion object {
        private const val TAG = "UserViewModel"
        private const val ERROR_MESSAGE_SHOW_TIME = 10_000L
    }

    private val listeners: MutableList<Listener> = mutableListOf()
    private val random: Random = Random()
    private val userList: MutableList<User> = select.from(User::class.java).queryList()

    var searchingUserInProgress = ObservableBoolean(false)
    var errorMessageVisible = ObservableBoolean(false)
    var errorMessage = ObservableField<String>("")

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun userCount() = userList.size
    fun singleUserViewModel(position: Int) = SingleUserViewModel(userList[position])
    fun onClickErrorMessage() = hideErrorMessage()

    fun addUserByApiToken(apiToken: String) = launch(UI) {

        try {
            searchingUserInProgress.set(true)

            val user: User? = async(CommonPool) { userService.addUserByApiToken(apiToken) }.await()

            if (user == null) {
                Log.w("UserViewModel", "Null user")
            } else {
                userList.add(user)
                listeners.forEach { it.usersUpdated() }
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

    private fun showErrorMessage(msg: String?) {
        errorMessage.set(msg)
        errorMessageVisible.set(true)
    }

    private fun hideErrorMessage() {
        errorMessageVisible.set(false)
        errorMessage.set("")
    }

    interface Listener {
        fun usersUpdated()
    }
}