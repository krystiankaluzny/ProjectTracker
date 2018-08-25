package app.obywatel.togglnative.model.service.timer

import android.util.Log
import org.ktoggl.TogglClient
import org.ktoggl.request.DetailedReportParameters
import app.obywatel.togglnative.model.entity.*
import app.obywatel.togglnative.model.service.toEntity
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite.select
import org.threeten.bp.OffsetDateTime

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

            val filteredTimeEntries = togglClient.getDetailedReport(it.id, DetailedReportParameters())
                .detailedTimeEntries
                .filter { it.project != null && projectsById.containsKey(it.project!!.id) }

            val timeEntryEntities: List<TimeEntry> = filteredTimeEntries
                .map { it.toEntity(projectsById[it.project!!.id]!!.first()) }
                .onEach { it.save() }

            user.lastSynchronizationTime = OffsetDateTime.now()
            user.save()

            Log.d(TAG, "fetchTimeEntries: $filteredTimeEntries")

            val nowTimestamp = OffsetDateTime.now().toEpochSecond()
            val timeEntry = org.ktoggl.entity.TimeEntry(
                description = "Dupa",
//                workspaceId = workspace.id,
                projectId = projectsById.keys.first(),
                startTimestamp = nowTimestamp,
                durationSeconds = -nowTimestamp,
                tags = listOf("Dupa", "test", "abc")
            )
            val createTimeEntriy = togglClient.createTimeEntry(timeEntry)
            Log.d(TAG, "fetchTimeEntries: $createTimeEntriy")
        }
    }
}

private fun List<Project>.contains(projectId: Long) = this.find { it.id == projectId } != null