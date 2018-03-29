package app.obywatel.togglnative.viewmodel.user

import android.database.sqlite.SQLiteConstraintException
import app.obywatel.togglnative.model.entity.User
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import java.util.*

class UsersViewModel {

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

        val newUser = User(random.nextLong(), apiToken)

        try {
            if (newUser.save()) {
                userList.add(newUser)
                listeners.forEach { it.usersUpdated() }
            }
        } catch (e: SQLiteConstraintException) {
            listeners.forEach { it.error(e.message) }
        }
    }

    interface Listener {
        fun usersUpdated()
        fun error(message: String?)
    }
}