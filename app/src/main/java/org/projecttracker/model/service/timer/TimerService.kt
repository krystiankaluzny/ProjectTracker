package org.projecttracker.model.service.timer

import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite.delete
import com.raizlabs.android.dbflow.sql.language.SQLite.select
import org.ktoggl.TogglClient
import org.ktoggl.entity.ProjectParent
import org.ktoggl.entity.StartTimeEntryData
import org.ktoggl.entity.UpdateTimeEntryData
import org.ktoggl.request.BaseReportParameters
import org.ktoggl.request.DetailedReportParameters
import org.projecttracker.model.entity.*
import org.projecttracker.model.service.toEntity
import org.slf4j.LoggerFactory
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

class TimerService(private val user: User, private val togglClient: TogglClient) {

    companion object {
        private val logger = LoggerFactory.getLogger(TimerService::class.java)
    }

    private var lastStartedTimeEntry: TimeEntry? = null

    // @formatter:off

    fun getStoredProjects(): MutableList<Project> = select()
                                                    .from(Project::class.java)
                                                    .where(Project_Table.workspace_id.eq(user.activeWorkspaceId))
                                                    .list

    // @formatter:on

    fun getStoredTimeEntriesForToday(): MutableList<TimeEntry> {

        val toTime = OffsetDateTime.now()
        val fromTime = toTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime()

        // @formatter:off
        return select()
               .from(TimeEntry::class.java)
               .where(TimeEntry_Table.startDateTime.between(fromTime).and(toTime))
               .list
        // @formatter:on
    }

    fun getStoredTimeEntriesForToday(project: Project): MutableList<TimeEntry> {

        val toTime = OffsetDateTime.now()
        val fromTime = toTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime()

        // @formatter:off
        return select()
               .from(TimeEntry::class.java)
               .where(TimeEntry_Table.project_id.eq(project.id))
               .and(TimeEntry_Table.startDateTime.between(fromTime).and(toTime))
               .list
        // @formatter:on
    }

    fun fetchTodayTimeEntries() {

        val toTime = OffsetDateTime.now()
        val fromTime = toTime.minusDays(1)

        synchronizeTimeEntries(fromTime, toTime)
    }

    fun startTimerForProject(project: Project) {

        val currentTime = OffsetDateTime.now()
        logger.debug("$project")

        val startTimeEntry = togglClient.startTimeEntry(
            StartTimeEntryData(
                parent = ProjectParent(project.id),
                description = project.name
            ))

        val updateTimeEntryData = UpdateTimeEntryData(
            startTimestamp = currentTime.toEpochSecond(),
            durationSeconds = -currentTime.toEpochSecond()
        )

        val startedTimeEntry = togglClient.updateTimeEntry(startTimeEntry.id, updateTimeEntryData)
            .toEntity(project)

        startedTimeEntry.save()

        lastStartedTimeEntry = startedTimeEntry
    }

    fun stopTimerForProject(project: Project) {

        lastStartedTimeEntry?.also {

            if (it.project?.id != project.id) {
                logger.warn("Stopped time entry in project: ${it.project} but it should be: $project")
            }

            val currentTime = OffsetDateTime.now()

            val start = it.startDateTime!!.toEpochSecond()
            val stop = currentTime.toEpochSecond()
            val updateTimeEntryData = UpdateTimeEntryData(
                startTimestamp = start,
                durationSeconds = stop - start,
                endTimestamp = stop
            )

            val stoppedTimeEntry = togglClient.updateTimeEntry(it.id, updateTimeEntryData)
                .toEntity(project)

            stoppedTimeEntry.save()
        }

        lastStartedTimeEntry = null
    }

    fun synchronizeTimeEntries() {

        val fromTime = user.lastSynchronizationTime ?: OffsetDateTime.now().minusMonths(1)
        val toTime = OffsetDateTime.now()

        synchronizeTimeEntries(fromTime, toTime)

        user.lastSynchronizationTime = toTime
        user.save()
    }

    private fun synchronizeTimeEntries(fromTime: OffsetDateTime, toTime: OffsetDateTime) {

        val workspace = select().from(Workspace::class.java).where(Workspace_Table.id.eq(user.activeWorkspaceId)).result

        workspace?.let { w ->

            val projectsById: Map<Long, List<Project>> = togglClient.getWorkspaceProjects(user.activeWorkspaceId)
                .map { it.toEntity(workspace) }
                .onEach { it.save() }
                .groupBy { it.id }

            val baseReportParameters = BaseReportParameters(
                fromTimestamp = fromTime.toEpochSecond(),
                toTimestamp = toTime.toEpochSecond())

            val filteredTimeEntries = togglClient.getDetailedReport(w.id, DetailedReportParameters(baseReportParameters))
                .detailedTimeEntries
                .filter { it.project != null && projectsById.containsKey(it.project!!.id) }

            delete(TimeEntry::class.java)
                .where(TimeEntry_Table.startDateTime.between(fromTime).and(toTime))
                .execute()

            filteredTimeEntries
                .map { it.toEntity(projectsById.getValue(it.project!!.id).first()) }
                .onEach { it.save() }

            logger.debug("fetchTimeEntries: $filteredTimeEntries")
        }
    }
}
