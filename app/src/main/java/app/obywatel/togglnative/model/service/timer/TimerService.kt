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

            togglClient.getWorkspaceProjects(user.activeWorkspaceId).forEach {
                Log.d(TAG, "Save project: $it")
                it.toEntity(workspace).save()
            }

            val detailedReport = togglClient.getDetailedReport(it.id, DetailedReportParameters())
            Log.d(TAG, "fetchTimeEntries: $detailedReport")
        }
    }
}