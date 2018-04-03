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

        val savedUserEntity: User? = SQLite.select().from(User::class).where(User_Table.apiToken eq apiToken).result

        if (savedUserEntity != null) {
            userEntity?.update()
        } else {
            userEntity?.save()
        }

        return userEntity
    }
}