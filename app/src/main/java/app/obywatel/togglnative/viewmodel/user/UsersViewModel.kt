package app.obywatel.togglnative.viewmodel.user

import android.databinding.ObservableBoolean
import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.UsersService
import com.raizlabs.android.dbflow.kotlinextensions.select
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class UsersViewModel(private val usersService: UsersService) {

    private val listeners: MutableList<Listener> = mutableListOf()
    private val random: Random = Random()
    private val userList: MutableList<User> = select.from(User::class.java).queryList()

    var searchingUserInProgress = ObservableBoolean(false)

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun userCount() = userList.size
    fun singleUserViewModel(position: Int) = SingleUserViewModel(userList[position])

    fun addUserByApiToken(apiToken: String) {

        launch(UI) {
            searchingUserInProgress.set(true)

            val user: User? = async(CommonPool) { usersService.addUserByApiToken(apiToken) }.await()

            if (user == null) {
                Log.w("UsersViewModel", "Null user")
            } else {
                userList.add(user)
                listeners.forEach { it.usersUpdated() }
            }

            searchingUserInProgress.set(false)
        }
    }

    interface Listener {
        fun usersUpdated()
        fun error(message: String?)
    }
}