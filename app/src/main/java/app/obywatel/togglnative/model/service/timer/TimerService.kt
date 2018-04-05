package app.obywatel.togglnative.model.service.timer

import android.util.Log
import app.obywatel.togglnative.model.entity.*
import app.obywatel.togglnative.model.service.toEntity
import ch.simas.jtoggl.JToggl
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite.select

class TimerService(private val user: User, private val jToggl: JToggl) {

    companion object {
        private val TAG = "TimerService"
    }

    // @formatter:off
    fun getStoredWorkspaces(): MutableList<Workspace> = select()
                                                        .from(Workspace::class.java)
                                                        .where(Workspace_Table.user_id.eq(user.id))
                                                        .list

    fun getStoredProjects(workspace: Workspace): MutableList<Project> = select()
                                                                        .from(Project::class.java)
                                                                        .where(Project_Table.workspace_id.eq(workspace.id))
                                                                        .list
    // @formatter:on


    fun fetchWorkspaces() {

        jToggl.workspaces.forEach {
            Log.d(TAG, "Save workspace: $it")
            it.toEntity(user).save()
        }
    }

    fun fetchProjects(workspace: Workspace) {

        jToggl.getWorkspaceProjects(workspace.id).forEach {
            Log.d(TAG, "Save project: $it")
            it.toEntity(workspace).save()
        }
    }
}