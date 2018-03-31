package app.obywatel.togglnative.model.service

import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.User_Table
import com.raizlabs.android.dbflow.kotlinextensions.*

class UsersService(private val jTogglFactory: JTogglFactory) {

    fun addUserByApiToken(apiToken: String): User? {

        val jToggl = jTogglFactory.jToggl(apiToken)

        var userEntity: User? = jToggl.currentUser?.toEntity()

        val savedUserEntity: User? = (select from User::class where (User_Table.apiToken eq apiToken)).result

        if (savedUserEntity != null) {
            userEntity?.update()
        } else {
            userEntity?.save()
        }

        return userEntity
    }

}
