package app.obywatel.togglnative.model.service.user

import android.util.Log
import app.obywatel.toggl.client.TogglClientBuilder
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.toEntity
import com.raizlabs.android.dbflow.kotlinextensions.save

class AddingUserService(private val togglClientBuilder: TogglClientBuilder) {

    companion object {
        private val TAG = "AddingUserService"
    }

    fun addUserByApiToken(apiToken: String): User? {

        val togglClient = togglClientBuilder.build(apiToken)

        val userEntity: User? = togglClient.getCurrentUser()?.toEntity()

        userEntity?.save()

        userEntity?.let {
            togglClient.getWorkspaces().forEach {
                Log.d(TAG, "Save workspace: $it")
                it.toEntity(userEntity).save()
            }
        }


        return userEntity
    }
}