package org.projecttracker.model.service.user

import org.ktoggl.TogglClientBuilder
import org.projecttracker.model.entity.User
import org.projecttracker.model.entity.User_Table
import org.projecttracker.model.entity.Workspace
import org.projecttracker.model.entity.Workspace_Table
import org.projecttracker.model.service.toEntity
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.SQLite.select
import com.raizlabs.android.dbflow.sql.language.SQLite.update
import org.slf4j.LoggerFactory

class UserService(private val togglClientBuilder: TogglClientBuilder) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }

    fun getAllUsers(): MutableList<User> = select().from(User::class).list

    fun selectUser(user: User) {
        user.selected = true
        user.update()

        update(User::class.java)
            .set(User_Table.selected eq false)
            .where(User_Table.id notEq user.id)
            .execute()
    }

    // @formatter:off
    fun getSelectedUser(): User? = select()
                                       .from(User::class)
                                       .where(User_Table.selected eq true)
                                       .result

    // @formatter:on

    fun addUserByApiToken(apiToken: String): User? {

        val togglClient = togglClientBuilder.build(apiToken)

        val userEntity: User? = togglClient.getCurrentUser().toEntity()

        userEntity?.save()

        userEntity?.let {
            togglClient.getWorkspaces().forEach {
                logger.debug("Save workspace: $it")
                it.toEntity(userEntity).save()
            }
        }

        return userEntity
    }

    // @formatter:off
    fun getStoredWorkspaces(user: User): MutableList<Workspace> = select()
                                                        .from(Workspace::class.java)
                                                        .where(Workspace_Table.user_id.eq(user.id))
                                                        .list
    // @formatter:on

    fun fetchWorkspaces(user: User) {

        val togglClient = togglClientBuilder.build(user.apiToken)
        togglClient.getWorkspaces().forEach {
            logger.debug("Save workspace: $it")
            it.toEntity(user).save()
        }
    }

    fun setActiveWorkspace(user: User, workspace: Workspace) {

        if (user.activeWorkspaceId == workspace.id) return

        if (workspace.user != null) {
            if (workspace.user?.id == user.id) {

                user.activeWorkspaceId = workspace.id
                user.save()
            }

        } else {
            val isUserWorkspace = select()
                .from(Workspace::class.java)
                .where(Workspace_Table.id.eq(workspace.id), Workspace_Table.user_id.eq(user.id))
                .list.isNotEmpty()

            if (isUserWorkspace) {
                user.activeWorkspaceId = workspace.id
                user.save()
            }
        }
    }
}
