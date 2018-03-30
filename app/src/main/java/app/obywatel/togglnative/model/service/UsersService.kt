package app.obywatel.togglnative.model.service

import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.User_Table
import com.raizlabs.android.dbflow.kotlinextensions.*
import java.util.*

class UsersService(private val jTogglFactory: JTogglFactory) {

    fun addUserByApiToken(apiToken: String): User? {

        val jToggl = jTogglFactory.jToggl(apiToken)

        var userEntity: User? = try {
            jToggl.currentUser?.toEntity()
        }
        catch (e: Exception) {
            Log.e("UsersService", "Current user error", e)
            null
        }

        userEntity = User(Random().nextInt().toLong(), apiToken)
        val savedUserEntity: User? = (select from User::class where (User_Table.apiToken eq apiToken)).result

        if (savedUserEntity != null) {
            userEntity?.update()
        } else {
            userEntity?.save()
        }

        return userEntity
    }

}
