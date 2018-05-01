package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglTimeEntityClient
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.internal.retrofit.TogglApi

internal class TogglTimeEntityClientImpl(private val togglApi: TogglApi) : TogglTimeEntityClient {

    override fun getTimeEntriy(timeEntryId: Long): TimeEntry {
    }

    override fun getRunningTimeEntriy(): TimeEntry? {
    }

    override fun createTimeEntriy(timeEntry: TimeEntry): TimeEntry {
    }

    override fun startTimeEntriy(timeEntry: TimeEntry): TimeEntry {
    }

    override fun stopTimeEntriy(timeEntry: TimeEntry): TimeEntry {
    }

    override fun updateTimeEntriy(timeEntry: TimeEntry): TimeEntry {
    }

    override fun deleteTimeEntriy(timeEntryId: Long): Boolean {
    }

    override fun updateTimeEntriesTags(timeEntryIds: List<Long>, tags: List<String>, updateTagsAction: TogglTimeEntityClient.UpdateTagsAction) {
    }
}