package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglTimeEntityClient
import app.obywatel.toggl.client.defaultIfBlank
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.internal.retrofit.TogglApi
import app.obywatel.toggl.client.internal.retrofit.dto.TimeEntryRequest

internal class TogglTimeEntityClientImpl(private val togglApi: TogglApi) : TogglTimeEntityClient {

    companion object { private const val TAG = "TogglTimeEntityClient" }

    override fun getTimeEntriy(timeEntryId: Long): TimeEntry {
        return TimeEntry(startTimestamp = 12L)
    }

    override fun getRunningTimeEntriy(): TimeEntry? {
        return null
    }

    override fun createTimeEntry(timeEntry: TimeEntry): TimeEntry? {
        val createdWith = timeEntry.createdWith.defaultIfBlank(TAG)
        val internalTimeEntry = timeEntry.toInternal().copy(id = null, created_with = createdWith)
        return togglApi.createTimeEntry(TimeEntryRequest(internalTimeEntry)).execute().body()?.timeEntry?.toExternal()
    }

    override fun startTimeEntriy(timeEntry: TimeEntry): TimeEntry {
        return timeEntry
    }

    override fun stopTimeEntriy(timeEntry: TimeEntry): TimeEntry {
        return timeEntry
    }

    override fun updateTimeEntriy(timeEntry: TimeEntry): TimeEntry {
        return timeEntry
    }

    override fun deleteTimeEntriy(timeEntryId: Long): Boolean {
        return false
    }

    override fun updateTimeEntriesTags(timeEntryIds: List<Long>, tags: List<String>, updateTagsAction: TogglTimeEntityClient.UpdateTagsAction) {
    }
}