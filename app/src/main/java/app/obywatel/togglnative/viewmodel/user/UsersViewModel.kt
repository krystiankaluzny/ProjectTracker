package app.obywatel.togglnative.viewmodel.user

import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.UsersService
import com.raizlabs.android.dbflow.kotlinextensions.select
import java.util.*

class UsersViewModel(private val usersService: UsersService) {

    private val listeners: MutableList<Listener> = mutableListOf()
    private val random: Random = Random()
    private val userList: MutableList<User> = select.from(User::class.java).queryList()

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun userCount() = userList.size
    fun singleUserViewModel(position: Int) = SingleUserViewModel(userList[position])

    fun addUserByApiToken(apiToken: String) {

        val user: User? = usersService.addUserByApiToken("sdf")

        if (user == null) {
            Log.w("UsersViewModel","Null user")
        }
        else {
            userList.add(user)
        }
    }

    interface Listener {
        fun usersUpdated()
        fun error(message: String?)
    }
}