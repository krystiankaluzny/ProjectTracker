package app.obywatel.togglnative.model.service.user

import app.obywatel.toggl.client.TogglClientBuilder
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.toEntity
import com.raizlabs.android.dbflow.kotlinextensions.*

class AddingUserService(private val togglClientBuilder: TogglClientBuilder) {

    fun addUserByApiToken(apiToken: String): User? {

        val togglClient = togglClientBuilder.build(apiToken)

        val userEntity: User? = togglClient.currentUser?.toEntity()

        userEntity?.save()

        return userEntity
    }
}