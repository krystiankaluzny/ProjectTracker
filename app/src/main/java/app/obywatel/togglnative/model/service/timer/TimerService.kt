package app.obywatel.togglnative.model.service.timer

import android.util.Log
import app.obywatel.toggl.client.TogglClient
import app.obywatel.toggl.client.request.DetailedReportParameters
import app.obywatel.togglnative.model.entity.*
import app.obywatel.togglnative.model.service.toEntity
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite.select

class TimerService(private val user: User, private val togglClient: TogglClient) {

    companion object {
        private val TAG = "TimerService"
    }

    // @formatter:off

    fun getStoredProjects(): MutableList<Project> = select()
                                                    .from(Project::class.java)
                                                    .where(Project_Table.workspace_id.eq(user.activeWorkspaceId))
                                                    .list
    // @formatter:on


    fun fetchTimeEntries() {

        val workspace = select().from(Workspace::class.java).where(Workspace_Table.id.eq(user.activeWorkspaceId)).result

        workspace?.let {

            val projectsById: Map<Long, List<Project>> = togglClient.getWorkspaceProjects(user.activeWorkspaceId)
                .map { it.toEntity(workspace) }
                .onEach { it.save() }
                .groupBy { it.id }

            val projectsTimeEntries = togglClient.getDetailedReport(it.id, DetailedReportParameters())
                .timeEntries
                .filter { it.project != null && projectsById.containsKey(it.project!!.id) }

            Log.d(TAG, "fetchTimeEntries: $projectsTimeEntries")
        }
    }
}

private fun List<Project>.contains(projectId: Long) = this.find { it.id == projectId } != null