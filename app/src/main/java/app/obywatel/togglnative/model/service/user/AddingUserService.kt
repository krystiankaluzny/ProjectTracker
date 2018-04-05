package app.obywatel.togglnative.model.service.user

import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.User_Table
import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.toEntity
import ch.simas.jtoggl.JToggl
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.SQLite

class AddingUserService(private val jTogglFactory: JTogglFactory) {

    fun addUserByApiToken(apiToken: String): User? {

        val jToggl: JToggl = jTogglFactory.jToggl(apiToken)

        val userEntity: User? = jToggl.currentUser?.toEntity()

        userEntity?.save()

        return userEntity
    }
}