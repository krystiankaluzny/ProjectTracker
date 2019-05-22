package org.projecttracker.model.service.timer

import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.or
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
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

class TimerService(private val user: User, private val togglClient: TogglClient) {

    companion object {
        private val logger = LoggerFactory.getLogger(TimerService::class.java)
    }

    // @formatter:off

    fun getStoredProjects(): MutableList<Project> = select()
                                                    .from(Project::class.java)
                                                    .where(Project_Table.workspace_id.eq(user.activeWorkspaceId))
                                                    .list

    // @formatter:on


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

        val toTime = OffsetDateTime.now().plusDays(1)
        val fromTime = toTime.minusDays(2)

        synchronizeFinishedTimeEntries(fromTime, toTime)
        synchronizeCurrentRunningTimeEntry()
    }

    fun startTimerForProjectAt(project: Project, currentTime: OffsetDateTime) {

        stopTimerAt(currentTime)

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
    }

    fun stopTimerAt(currentTime: OffsetDateTime) {

        synchronizeCurrentRunningTimeEntry()

        val currentRunningTimeEntry = select()
            .from(TimeEntry::class.java)
            .where(TimeEntry_Table.project_id.`in`(getStoredProjects().map { it.id }))
            .and(TimeEntry_Table.startDateTime.isNotNull)
            .and(TimeEntry_Table.endDateTime.isNull.or(TimeEntry_Table.duration.lessThan(Duration.ZERO)))
            .orderBy(TimeEntry_Table.lastUpdateDateTime.desc())
            .orderBy(TimeEntry_Table.id.desc())
            .result

        logger.info("toggleProject id {}", currentRunningTimeEntry?.project?.id)
        currentRunningTimeEntry?.also {

            val start = it.startDateTime!!.toEpochSecond()
            val stop = currentTime.toEpochSecond()
            if (stop == start) {

                logger.debug("delete current task {}", it.id)

                delete()
                    .from(TimeEntry::class.java)
                    .where(TimeEntry_Table.id.eq(it.id))

                togglClient.deleteTimeEntry(it.id)

            } else {

                logger.debug("update current task {}", it.id)

                val updateTimeEntryData = UpdateTimeEntryData(
                    startTimestamp = start,
                    durationSeconds = stop - start,
                    endTimestamp = stop
                )

                val stoppedTimeEntry = togglClient.updateTimeEntry(it.id, updateTimeEntryData)
                    .toEntity(it.project!!)

                stoppedTimeEntry.save()
            }
        }
    }

    private fun synchronizeFinishedTimeEntries(fromTime: OffsetDateTime, toTime: OffsetDateTime) {


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

    private fun synchronizeCurrentRunningTimeEntry() {

        delete()
            .from(TimeEntry::class.java)
            .where(TimeEntry_Table.startDateTime.isNotNull)
            .and(TimeEntry_Table.endDateTime.isNull.or(TimeEntry_Table.duration.lessThan(Duration.ZERO)))
            .execute()

        togglClient.getCurrentTimeEntry()?.also {

            if (it.projectId == null) {
                logger.error("time entry {} have null project id", it)
                return@also
            }

            val project = select()
                .from(Project::class.java)
                .where(Project_Table.id.eq(it.projectId))
                .result

            if (project == null) {
                logger.error("Project with id {} doesn't found", it.projectId)
                return@also
            }

            it.toEntity(project).save()
        }
    }
}
